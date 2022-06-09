/*<?php /**/

define('LARAVEL_START', microtime(true));

echo "%%START-OUTPUT%%";

$projectSettings = json_decode($argv[2]) ?? new stdClass();

require __DIR__ . '/vendor/autoload.php';
$app = require_once __DIR__ . '/bootstrap/app.php';
$kernel = $app->make(\Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

$config = new \Psy\Configuration([
    'updateCheck' => 'never',
    'usePcntl' => false,
    'useReadline' => false,
    'prompt' => ''
]);

$casters = [
    'Illuminate\Support\Collection' => 'Laravel\Tinker\TinkerCaster::castCollection',
    'Illuminate\Support\HtmlString' => 'Laravel\Tinker\TinkerCaster::castHtmlString',
    'Illuminate\Support\Stringable' => 'Laravel\Tinker\TinkerCaster::castStringable',
    'Illuminate\Database\Eloquent\Model' => 'Laravel\Tinker\TinkerCaster::castModel',
    'Illuminate\Foundation\Application' => 'Laravel\Tinker\TinkerCaster::castApplication',
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

$shell = new \Psy\Shell($config);

$output = new \Symfony\Component\Console\Output\ConsoleOutput();
$shell->setOutput($output);

$autoloadClassMap = __DIR__ . '/vendor/composer/autoload_classmap.php';
if(class_exists('\Laravel\Tinker\ClassAliasAutoloader')) {
    $loader = \Laravel\Tinker\ClassAliasAutoloader::register($shell, $autoloadClassMap);
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
$shell->addInput('echo "%%END-OUTPUT%%";', true);
if($projectSettings->terminateApp) {
    $shell->addInput('app()->terminate()', true);
}
$shell->addInput('throw new \Psy\Exception\BreakException("%%END-OUTPUT%%");', true);
$closure = new \Psy\ExecutionLoopClosure($shell);
$closure->execute();

if(isset($loader)) {
    $loader->unregister();
}
