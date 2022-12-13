/*<?php /**/

define('LARAVEL_START', microtime(true));

$projectSettings = json_decode($argv[2]) ?? new stdClass();

require ($projectSettings->vendorRoot ?: __DIR__) . '/vendor/autoload.php';
$app = require_once ($projectSettings->laravelRoot ?: __DIR__) . '/bootstrap/app.php';
$kernel = $app->make(\Illuminate\Contracts\Console\Kernel::class);
$kernel->bootstrap();

$keywordColor = $projectSettings->keywordColor ?? 'rgb(0,170,170)';
$intColor = $projectSettings->intColor ?? 'rgb(188,63,188)';
$stringColor = $projectSettings->stringColor ?? 'rgb(57,181,74)';
$floatColor = $projectSettings->floatColor ?? 'rgb(128,128,0)';


$config = new \Psy\Configuration([
    'updateCheck' => 'never',
    'usePcntl' => false,
    'useReadline' => false,
    'prompt' => '',
    'theme' => 'compact',
]);
$config->setRawOutput(true);
$config->setColorMode(\Psy\Configuration::COLOR_MODE_FORCED);
$config->setInteractiveMode(\Psy\Configuration::INTERACTIVE_MODE_DISABLED);

$casters = [
    'Illuminate\Support\Collection' => 'Laravel\Tinker\TinkerCaster::castCollection',
    'Illuminate\Support\HtmlString' => 'Laravel\Tinker\TinkerCaster::castHtmlString',
    'Illuminate\Support\Stringable' => 'Laravel\Tinker\TinkerCaster::castStringable',
    'Illuminate\Database\Eloquent\Model' => 'Laravel\Tinker\TinkerCaster::castModel',
    'Illuminate\Foundation\Application' => 'Laravel\Tinker\TinkerCaster::castApplication',
];

$existingCasters = [];
foreach ($casters as $castableClass => $casterMethod) {
    list($casterClass, $casterClassMethod) = explode('::', $casterMethod);
    if (class_exists($castableClass) && method_exists($casterClass, $casterClassMethod)) {
        $existingCasters[$castableClass] = $casterMethod;
    }
}

$config->getPresenter()->addCasters($existingCasters);
$config->setHistoryFile(defined('PHP_WINDOWS_VERSION_BUILD') ? 'nul' : '/dev/null');

$outputStream = fopen('php://memory', 'w+');

$shell = new \Psy\Shell($config);
$output = new \Symfony\Component\Console\Output\StreamOutput($outputStream);
$shell->setOutput($output);
$closure = new \Psy\ExecutionLoopClosure($shell);

$autoloadClassMap = ($projectSettings->vendorRoot ?: __DIR__) . '/vendor/composer/autoload_classmap.php';
if (class_exists('\Laravel\Tinker\ClassAliasAutoloader')) {
    $loader = \Laravel\Tinker\ClassAliasAutoloader::register($shell, $autoloadClassMap);
}

$unsanitizedRunCode = token_get_all($argv[1]);
$sanitizedRunCode = '';
foreach ($unsanitizedRunCode as $token) {
    if (!is_string($token)) {
        [$id, $token] = $token;
        if (in_array($id, [T_COMMENT, T_DOC_COMMENT, T_OPEN_TAG, T_OPEN_TAG_WITH_ECHO, T_CLOSE_TAG], true)) {
            continue;
        }
    }

    $sanitizedRunCode .= $token;
}

$shell->addInput($sanitizedRunCode, true);
if ($projectSettings->terminateApp) {
    $shell->addInput('echo "%%END-OUTPUT%%";', true);
    $shell->addInput('app()->terminate();', true);
}
$shell->addInput('usleep(250000); echo "%%END-OUTPUT%%"; throw new \Psy\Exception\BreakException("Goodbye");', true);
$closure->execute();

if (isset($loader)) {
    $loader->unregister();
}

rewind($outputStream);
$output = stream_get_contents($outputStream);
fclose($outputStream);

$splitOutput = preg_split('/(\x1b\[[0-9;]*m)/', $output, -1, PREG_SPLIT_DELIM_CAPTURE);

$htmlOutput = '<span>';
$color = null;
$bold = false;
$underline = false;
$textBuffer = '';

foreach ($splitOutput as $ansiOrText) {
    if (preg_match('/\x1b\[([0-9;]*)m/', $ansiOrText, $matches)) {
        if ($matches[1] == '0') {
            $color = null;
            $bold = false;
            $underline = false;
        } elseif ($matches[1] == '1') {
            $bold = true;
        } elseif ($matches[1] == '4') {
            $underline = true;
        } elseif ($matches[1] == '22') {
            $bold = false;
        } elseif ($matches[1] == '24') {
            $underline = false;
        } elseif ($matches[1] == '32') {
            $color = $stringColor;
        } elseif ($matches[1] == '33') {
            $color = $floatColor;
        } elseif ($matches[1] == '35') {
            $color = $intColor;
        } elseif ($matches[1] == '36') {
            $color = $keywordColor;
        } elseif ($matches[1] == '39') {
            $color = null;
        }

        $style = "";
        if ($color) {
            $style .= "color:$color;";
        }
        if ($bold) {
            $style .= "font-weight:bold;";
        }
        if ($underline) {
            $style .= "text-decoration:underline;";
        }

        $htmlOutput .= '</span>';
        $htmlOutput .= "<span style=\"$style\">";
    } else {
        $htmlOutput .= htmlspecialchars($ansiOrText);
    }
}

$htmlOutput .= '</span>';
$htmlOutput = preg_replace('/&lt;whisper&gt;/si', '<span style="color: gray;">', $htmlOutput);
$htmlOutput = preg_replace('/&lt;\/whisper&gt;/si', '</span>', $htmlOutput);

echo "%%START-OUTPUT%%";
echo trim($htmlOutput);
