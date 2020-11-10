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
if(class_exists('\Laravel\Tinker\ClassAliasAutoloader')) {
    $loader = \Laravel\Tinker\ClassAliasAutoloader::register($shell, $autoloadClassMap);
}

$code = array_reduce(
    token_get_all($argv[1]),
    function ($carry, $token) {
        if (is_string($token)) {
            return $carry . $token;
        }

        [$id, $text] = $token;

        if (in_array($id, [T_COMMENT, T_DOC_COMMENT, T_OPEN_TAG, T_OPEN_TAG_WITH_ECHO, T_CLOSE_TAG], true)) {
            $text = '';
        }

        return $carry . $text;
    },
    ''
);

$shell->addInput($code, true);
$closure = new \Psy\ExecutionLoopClosure($shell);
$closure->execute();

if(isset($loader)) {
    $loader->unregister();
}
