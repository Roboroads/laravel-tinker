/*<?php /**/

use Illuminate\Contracts\Console\Kernel;
use Laravel\Tinker\ClassAliasAutoloader;
use Psy\Configuration;
use Psy\ExecutionLoopClosure;
use Psy\Shell;
use Symfony\Component\Console\Output\ConsoleOutput;
use Symfony\Component\VarDumper\Caster\ReflectionCaster;
use Symfony\Component\VarDumper\Cloner\VarCloner;
use Symfony\Component\VarDumper\Dumper\CliDumper;
use Symfony\Component\VarDumper\VarDumper;

define('LARAVEL_START', microtime(true));

echo PHP_EOL;

$projectSettings = json_decode($argv[2]) ?? new stdClass();

require ($projectSettings->vendorRoot ?: __DIR__) . '/vendor/autoload.php';
$app = require_once ($projectSettings->laravelRoot ?: __DIR__) . '/bootstrap/app.php';
$kernel = $app->make(Kernel::class);
$kernel->bootstrap();

$config = new Configuration([
    'updateCheck' => 'never',
    'usePcntl' => false,
    'useReadline' => false,
    'prompt' => '',
    'theme' => 'compact',
]);
$config->setRawOutput(true);
$config->setColorMode(Configuration::COLOR_MODE_FORCED);
$config->setInteractiveMode(Configuration::INTERACTIVE_MODE_DISABLED);

$casters = [
    'Illuminate\Support\Collection' => 'Laravel\Tinker\TinkerCaster::castCollection',
    'Illuminate\Support\HtmlString' => 'Laravel\Tinker\TinkerCaster::castHtmlString',
    'Illuminate\Support\Stringable' => 'Laravel\Tinker\TinkerCaster::castStringable',
    'Illuminate\Database\Eloquent\Model' => 'Laravel\Tinker\TinkerCaster::castModel',
    'Illuminate\Foundation\Application' => 'Laravel\Tinker\TinkerCaster::castApplication',
    'Illuminate\Process\ProcessResult' => 'Laravel\Tinker\TinkerCaster::castProcessResult',
];

$existingCasters = [];
foreach($casters as $castableClass => $casterMethod) {
    list($casterClass, $casterClassMethod) = explode('::', $casterMethod);
    if(class_exists($castableClass) && method_exists($casterClass, $casterClassMethod)) {
        $existingCasters[$castableClass] = $casterMethod;
    }
}

$config->getPresenter()->addCasters($existingCasters);
$config->setHistoryFile(defined('PHP_WINDOWS_VERSION_BUILD') ? 'nul' : '/dev/null');

class TinkerOutput extends ConsoleOutput
{
    public function extDoWrite(string $message, bool $newline): void
    {
        $this->doWrite($message, $newline);
    }

    protected function doWrite(string $message, bool $newline): void
    {
        parent::doWrite(base64_encode($message), $newline);
    }
}

$shell = new Shell($config);
$output = new TinkerOutput();
$shell->setOutput($output);
$closure = new ExecutionLoopClosure($shell);

// Fix DD/Dump
$cloner = new VarCloner();
$cloner->addCasters(ReflectionCaster::UNSET_CLOSURE_FILE_INFO);
$dumper = new CliDumper(function(string $line, int $depth, string $indentPad) use ($output) {
    if (-1 !== $depth) {
        $output->extDoWrite(str_repeat($indentPad, $depth).$line."\n", true);
    } else {
        $output->extDoWrite("", true);
    }
});
$dumper->setColors(true);
VarDumper::setHandler(function($var) use ($cloner, $dumper) {
    $dumper->dump($cloner->cloneVar($var));
});

$autoloadClassMap = ($projectSettings->vendorRoot ?: __DIR__) . '/vendor/composer/autoload_classmap.php';
if(class_exists(ClassAliasAutoloader::class)) {
    $loader = ClassAliasAutoloader::register($shell, $autoloadClassMap);
}

$unsanitizedRunCode = token_get_all($argv[1]);
$sanitizedRunCode = '';
foreach($unsanitizedRunCode as $token) {
    if (!is_string($token)) {
        [$id, $token] = $token;
        if (in_array($id, [T_COMMENT, T_DOC_COMMENT, T_OPEN_TAG, T_OPEN_TAG_WITH_ECHO, T_CLOSE_TAG], true)) {
            continue;
        }
    }

    $sanitizedRunCode .= $token;
}

$shell->addInput($sanitizedRunCode, true);
if($projectSettings->terminateApp) {
    $shell->addInput('app()->terminate();', true);
}
$shell->addInput('usleep(250000); throw new \Psy\Exception\BreakException();', true);
$closure->execute();

if(isset($loader)) {
    $loader->unregister();
}
