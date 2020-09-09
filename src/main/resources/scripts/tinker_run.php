/*<?php/**/

define('LARAVEL_START', microtime(true));

echo "%%START-OUTPUT%%";

require __DIR__ . '/vendor/autoload.php';
$app = require_once __DIR__ . '/bootstrap/app.php';
$kernel = $app->make(\Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

$config = new \Psy\Configuration([
    'updateCheck' => 'never',
    'usePcntl' => false,
    'useReadline' => false,
    'prompt' => '%%EOT%%'
]);

$casters = [
    'Illuminate\Support\Collection' => 'Laravel\Tinker\TinkerCaster::castCollection',
    'Illuminate\Support\HtmlString' => 'Laravel\Tinker\TinkerCaster::castHtmlString',
];
if (class_exists('Illuminate\Database\Eloquent\Model')) {
    $casters['Illuminate\Database\Eloquent\Model'] = 'Laravel\Tinker\TinkerCaster::castModel';
}
if (class_exists('Illuminate\Foundation\Application')) {
    $casters['Illuminate\Foundation\Application'] = 'Laravel\Tinker\TinkerCaster::castApplication';
}

$config->getPresenter()->addCasters($casters);

$config->setHistoryFile(defined('PHP_WINDOWS_VERSION_BUILD') ? 'null' : '/dev/null');

$shell = new \Psy\Shell($config);

$output = new \Symfony\Component\Console\Output\ConsoleOutput();
$shell->setOutput($output);

$autoloadClassMap = __DIR__ . '/vendor/composer/autoload_classmap.php';
$loader = \Laravel\Tinker\ClassAliasAutoloader::register($shell, $autoloadClassMap);

$code = str_replace(['<?=', '<?php', '<?', '?>'], '', $argv[1]);
$shell->addInput($code, true);
$closure = new \Psy\ExecutionLoopClosure($shell);
$closure->execute();

$loader->unregister();
