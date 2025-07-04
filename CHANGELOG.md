# Changelog

All notable changes to this project will be documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed

- [[#400]](https://github.com/Roboroads/laravel-tinker/issues/400) Run line marker now also shows up at the last line of the tinker console.

## [3.0.0] - 2025-06-19

### Added

- Added file type, `*.tinker.php`. This will allow you to put scripts anywhere in your project and give them custom names.
  - This has resolved [[#393]](https://github.com/Roboroads/laravel-tinker/issues/393) indirectly as well.
  - This also has solved the confusing 3v4l.org run button above the console.
- Added per-project configuration option as to where to store the tinker consoles the plugin creates.
- The output windows now automatically opens/closes when switching to/away from a tinker console. (Thanks [@KamikX](https://github.com/KamikX)!)
- Added a "clear console" action. (Thanks [@KamikX](https://github.com/KamikX)!)

### Changed

- [[#330]](https://github.com/Roboroads/laravel-tinker/issues/330) Tinker consoles are now saved within the project, so you have per-project tinker consoles.
- [[#364]](https://github.com/Roboroads/laravel-tinker/issues/364) Changed Laravel project detection to use the existence of `venctor/laravel/framework/conposer.json` instead of checking in the composer.json file for the `laravel/framework` package.

### Fixed

- [[#392]](https://github.com/Roboroads/laravel-tinker/issues/392) Moved tinker actions to the BGT, removing the EDT error.
- New tinker consoles now always start with the `<?php` tag.
- Fixed the `OpenNewConsoleWithSelectionAction` using the current editors VirtualFile (Thanks [@KamikX](https://github.com/KamikX)!)

### Removed

- Laravel Tinker Console from the "Scratches and Consoles" menu. \
  ⚠️ BREAKING CHANGE: Your old tinker consoles are still available on the file system. You can find them in the `${idea.config.path}/laravel-tinker` directory. Your tinker consoles will *not* be automatically migrated to the new location, because the new location is per-project. You can copy them manually to the new location, or just create new tinker consoles.\
  _[Where is my idea.config.path?](https://intellij-support.jetbrains.com/hc/en-us/articles/206544519-Directories-used-by-the-IDE-to-store-settings-caches-plugins-and-logs)_

### Miscellaneous

- Updated dependencies to their latest versions, including the migration to Intellij Platform v2.

## [2.7.3] - 2024-06-19

### Fixed

- [[#349]](https://github.com/Roboroads/laravel-tinker/issues/349) Fixed a compatibility issue with PHP 7.0 (Laravel 5.5) and older versions of PsySH.
- [[#306]](https://github.com/Roboroads/laravel-tinker/issues/306) Put all laravel functionality behind a check to see if the project is a laravel project, making the plugin also
  work with plain PsySH without laravel or tinker installed.

## [2.7.2] - 2024-06-17

### Fixed

- [[#255]](https://github.com/Roboroads/laravel-tinker/issues/255)[[#280]](https://github.com/Roboroads/laravel-tinker/issues/280) Fixed by windows destroyed characters in a
  different way

## [2.7.1] - 2024-06-17

### Miscellaneous*

- Reverted the fix for windows users where Cyrillic and Arabic characters would be lost in encoding conversions.
    - Sorry to these users, I'm still waiting response from JetBrains to properly fix this issue. The workaround I implemented for 2.7 brings more issues than it fixes.

## [2.7.0] - 2024-04-09*

### BREAKING

- Your tinker plugin settings might reset after the update - sorry for that :)

### Fixed

- Swapped out deprecated StringEscapeUtils.
- Fixed ModalityState deprecation warning.
- [[#316]](https://github.com/Roboroads/laravel-tinker/issues/316) Fixed ActionUpdateThread deprecation.
- [[#312]](https://github.com/Roboroads/laravel-tinker/issues/312) Fixed app settings storage conflict with another plugin.
- [[#255]](https://github.com/Roboroads/laravel-tinker/issues/255)[[#280]](https://github.com/Roboroads/laravel-tinker/issues/280) Fixed (worked around) characters not present in
  windows-1252 charset being displayed weirdly on windows machines.
- Fixed deprecation warnings coming from the Migrations.

### Miscellaneous

- Updated all dependencies to their latest versions.
- Fixed a typo in feature request template. (Thanks [@igorsantos07](https://github.com/igorsantos07)!)
- Changed minimal version to 2024.1, deprecating the plugin for older versions of IDEs.

## [2.6.1] - 2023-05-08

### Fixed

- [[#252]](https://github.com/Roboroads/laravel-tinker/issues/252) Cast exception when running on an SSH interpreter.
- [[#253]](https://github.com/Roboroads/laravel-tinker/issues/253) Remove unneeded `withPtty: true` from process handler.
- Fixed an early return when no ansicommands are found in the output, that also not parsed out the whisper tag.

## [2.6.0] - 2023-04-16

### Added

- Added new `castProcessResult` tinker caster. (Thanks [@emargareten](https://github.com/emargareten)!)
- ANSI Parser for the output window within the Kotlin part of the plugin.
- Meta tag to set charset to UTF8 in toolwindow HTML.

### Changed

- Now using the ToolWindow without use of a .form file, which was not supported in combination with Kotlin (even though it always had worked in the past).

### Removed

- Reverted to the pre-2.5 tinker_run.php, since the new method didn't work in all cases.

### Miscellaneous

- Pulled missed changes from the Intellij platform plugin template.

## [2.5.1] - 2022-12-14

### Added

- [[#220]](https://github.com/Roboroads/laravel-tinker/issues/220) Add context menu action in tinker consoles to run the console.
- Replace JB run icon with a green tinker run icon.
- [[#223]](https://github.com/Roboroads/laravel-tinker/issues/223) Editor font size is now used as font size for the tinker console.

## [2.5.0] - 2022-12-13

### Added

- [[#190]](https://github.com/Roboroads/laravel-tinker/issues/190) Add way to customize vendor path. (Thanks [@bobisdacool1](https://github.com/bobisdacool1)!)
    - Also fixed the bug from this PR where it crashed if you didn't configure anything.
- [[#171]](https://github.com/Roboroads/laravel-tinker/issues/171) Add ANSI mode parsing (instead of regex-replace-guessing in the output window)

### Changed

- Due to demand: added more ways to support this plugin, like Ko-Fi and sharing options.
    - Adding these ways to a rotation for the support message every 10 executions.
    - Adding these ways to `FUNDING.yml`.
    - Generalize "Patereon" to "Support" in the code.

### Removed

- Rexeg-replace-guessing in the output window, replaced with ANSI mode parsing.
- [[#217]](https://github.com/Roboroads/laravel-tinker/issues/217) Removed the deprecated ProjectManagerListener.projectOpened event. Didn't replace it with a postStartupActivity
  because it apparently works without just fine.

### Fixed

- [[#212]](https://github.com/Roboroads/laravel-tinker/issues/212) Whisper tags now get parsed to a span with text color set to gray.
- "INFO" being left after execution because of Laravel's new error parsing in the console.

### Miscellaneous

- Updated dependencies (Thanks [@dependabot](https://github.com/apps/dependabot)!)
- Removed some now unused code due to removing of ProjectManagerListener.projectOpened event.

## [2.4.0]

### Added

- [[#169]](https://github.com/Roboroads/laravel-tinker/issues/169) Option to call app termination after running code.

### Fixed

- [[#178]](https://github.com/Roboroads/laravel-tinker/issues/178) Process exiting before full output is available.

## [2.3.4]

### Fixed

- [[#46]](https://github.com/Roboroads/laravel-tinker/issues/46) Execution on docker
    - ℹ️If it doesn't work, please [read this](https://github.com/Roboroads/laravel-tinker/issues/167).
    - ℹ️This does not fix the "string conversion" error found when using `docker exec`. You still have to use `docker run` for now.
- [[#162]](https://github.com/Roboroads/laravel-tinker/issues/162) Endless execution of the tinker process in several usecases (like JB 2022.2 EAP)
- Compatibility issue with JB 2022.2 (EAP) regarding `ContentFactory.SERVICE`

## [2.3.3]

### Removed

- [[#136]](https://github.com/Roboroads/laravel-tinker/issues/136) Upper version limit for JB products.
    - ℹ This will enable the plugin to be compatible for all JB products >= 211 until a compatibility issue shows up.

## [2.3.2]

### Changed

- Updated dependencies.

### Fixed

- Qodana version which stopped the build from completing

## [2.3.1]

## [2.3.0]

### Added

- [[#112]](https://github.com/Roboroads/laravel-tinker/issues/112) Adds <kbd>Open new console</kbd> and <kbd>Reopen last console</kbd> actions to the tinker output toolwindow.

### Changed

- Cleaned up the output toolwindow a little.
- Added dependabot updates [#113](https://github.com/Roboroads/laravel-tinker/pull/113) & [#114](https://github.com/Roboroads/laravel-tinker/pull/114).

### Fixed

- [[#110]](https://github.com/Roboroads/laravel-tinker/issues/110) Fixes typo in caster for the Model class.

## [2.2.2]

### Added

- Way of migrating to newer versions of this plugin.
- [[#105]](https://github.com/Roboroads/laravel-tinker/pull/105) Adds the stringable caster added in [laravel/tinker v2.6.1](https://github.com/laravel/tinker/pull/121). (
  Thanks [@emargareten](https://github.com/emargareten)!)

### Changed

- Restyling of Tinker console tabs.

### Fixed

- [[#61]](https://github.com/Roboroads/laravel-tinker/issues/61) Fixes re-indexing issue causing infinite errors and 100% CPU. (Thanks [@fawzanm](https://github.com/fawzanm)!)

## [2.2.1]

### Added

- [[#96]](https://github.com/Roboroads/laravel-tinker/issues/96) Added compatibility with newer API versions.

## [2.2.0]

### Added

- [[#34]](https://github.com/Roboroads/laravel-tinker/issues/34) Setting to change tinker execution root.

### Fixed

- [[#75]](https://github.com/Roboroads/laravel-tinker/issues/75) Empty directory list of tinker consoles resulting in fatal error.

## [2.1.0]

### Added

- Dependabot.
- [[#35]](https://github.com/Roboroads/laravel-tinker/issues/35) Setting to enable/disable line wrapping in output window.
- [[#33]](https://github.com/Roboroads/laravel-tinker/issues/33) Adds way for Patreon supporters to disable the support plug.

### Changed

- [[#53]](https://github.com/Roboroads/laravel-tinker/issues/53) Updates compatibility with 2021.1.
- Updated all dependencies.

### Removed

- Tinker Consoles having an IS_TINKER_CONSOLE userdata key.

### Fixed

- [[#47]](https://github.com/Roboroads/laravel-tinker/issues/47) [[#40]](https://github.com/Roboroads/laravel-tinker/issues/40) `<` and `>` are now replaced by `&gt;` and `&lt;` to
  make them not parse as HTML in tinker output.
- [[#40]](https://github.com/Roboroads/laravel-tinker/issues/40) Fixes always showing output on the last opened project when having multiple projects open.
- [[#41]](https://github.com/Roboroads/laravel-tinker/issues/41) When pressing the x icon on the progress bar to stop the PHP execution, the process now gets destroyed within
  250ms.

## [2.0.1]

### Changed

- New branding by [Joey "Veritas" de Vries](https://joeyveritas.nl).

### Fixed

- [[#50]](https://github.com/Roboroads/laravel-tinker/issues/50) Tinker Create/Smart action overwriting its own files from a previous session.
- Tinker files not being PHP when project reloads.

## [2.0.0]

### Added

- BREAKING: [[#39]](https://github.com/Roboroads/laravel-tinker/issues/39) Tinker consoles are now available under "Scratches and Consoles".
- [[#37]](https://github.com/Roboroads/laravel-tinker/issues/37) [[#38]](https://github.com/Roboroads/laravel-tinker/issues/38) When selecting text and running the `Smart Action` a
  new console opens with your selected text.
    - ℹ This can also be achieved by selecting text, right-clicking and selecting `Open Tinker Console With Selection`.

### Changed

- BREAKING: Every action is now independently listed.
    - ℹ The default action is now "Smart Action" which will have kind-of the same functionality as you have been using it up till now.
- Every action this plugin can do is now individually bindable in your keymap.

### Removed

- BREAKING: Per-project cache (which stored your last executed tinker code).
    - ℹ You can probably find your last snippet still in `%Project%/.idea/laravel-tinker.xml` if you really need it! `%Project%/.idea/laravel-tinker.xml` can be deleted otherwise.

### Other

- Reorganization of a lot of classes.
- Dependencies have been updated.

## [1.3.1]

### Changed

- Updated dependencies.

### Fixed

- Compatibility with 2020.3.

## [1.3.0]

### Added

- Laravel Tinker settings page (under Tools).
- [[#30]](https://github.com/Roboroads/laravel-tinker/issues/30) Ability to disable start and finish messages in output.
- [[#21]](https://github.com/Roboroads/laravel-tinker/issues/21) Ability to add a shortcut to close all involving Tinker.

### Changed

- Dependencies updated to the latest versions.

### Fixed

- The title of the tinker screen was editable - now it isn't.
- [[#28]](https://github.com/Roboroads/laravel-tinker/issues/28) Ending your tinker console with a comment now works correctly again. (
  Thanks [@emargareten](https://github.com/emargareten)!)

## [1.2.1]

### Added

- [[#25]](https://github.com/Roboroads/laravel-tinker/issues/25) When running tinker it will now save all documents before executing.

### Fixed

- [[#23]](https://github.com/Roboroads/laravel-tinker/issues/23) Fixed a bug where output would display twice when the output highlighter didn't find output to highlight.

## [1.2.0]

### Added

- [[#7]](https://github.com/Roboroads/laravel-tinker/issues/7) Output syntax highlighting.

### Fixed

- [[#11]](https://github.com/Roboroads/laravel-tinker/issues/11) Plugin won't create a null file anymore on windows.
- [[#16]](https://github.com/Roboroads/laravel-tinker/issues/16) Only 1 tinker console will be open at all times.
- [[#18]](https://github.com/Roboroads/laravel-tinker/issues/18) Added the LARAVEL_START constant to the tinker run script.
- [[#19]](https://github.com/Roboroads/laravel-tinker/issues/19) Colors in output window now reset to IDE colors when running.
- [[#12]](https://github.com/Roboroads/laravel-tinker/issues/12) [[#20]](https://github.com/Roboroads/laravel-tinker/issues/20)
  Fixes `Class 'Laravel\Tinker\ClassAliasAutoloader' not found`
    - ℹ Fixes the plugin for laravel/tinker versions `<= 1.0.1`.

## [1.1.1]

### Added

- [[#10]](https://github.com/Roboroads/laravel-tinker/issues/10) Your last command will now be saved (on per-project level).
- Non-obtrusive reminder you can support this package.

### Changed

- [[#6]](https://github.com/Roboroads/laravel-tinker/issues/6) Output window now (hopefully) always inherits the font and color of the IDE

### Fixed

- The link to PHP settings in error dialogs now *acually* links to the correct settings page.
- [[#9]](https://github.com/Roboroads/laravel-tinker/issues/9) Output showing slow on larger outputs.

## [1.1.0]

### Added

- Run button at the opening tag of the tinker console.

### Changed

- Cleaned output of tinker output window
- PHP Runner script is now a php file, makes it easier to work with in the IDE.

### Fixed

- Default keybinding in Mac OS.
- Toolwindow not opening automatically if not opened before.
- Leaving zombie processes (something pcntl did).
- Silent crash when docker was not set.
- Tinker process not shutting down because of waiting for non-existent input.

## [1.0.0]

### Added

- [[#1]](https://github.com/Roboroads/laravel-tinker/issues/1) A correct way to interact with remote interpreters.

### Changed

- Made errors more to the point.
- Better code splitting in message balloons.
- More text in the readme.
- Better formatting in tinker output.

### Fixed

- Laravel now actually gets bootstrapped.
- Removed needless output from interpreter, like docker startup messages.
- Made output toolwindow font show in readable colors.
- Campatibility issues that arose in 0.0.1-ALPHA.

## [0.0.1-ALPHA]

### Added

- Everything.

[Unreleased]: https://github.com/Roboroads/laravel-tinker/compare/v3.0.0...HEAD
[3.0.0]: https://github.com/Roboroads/laravel-tinker/compare/v2.7.3...v3.0.0
[2.7.3]: https://github.com/Roboroads/laravel-tinker/compare/v2.7.2...v2.7.3
[2.7.2]: https://github.com/Roboroads/laravel-tinker/compare/v2.7.1...v2.7.2
[2.7.1]: https://github.com/Roboroads/laravel-tinker/compare/v2.7.0...v2.7.1
[2.7.0]: https://github.com/Roboroads/laravel-tinker/compare/v2.6.1...v2.7.0
[2.6.1]: https://github.com/Roboroads/laravel-tinker/compare/v2.6.0...v2.6.1
[2.6.0]: https://github.com/Roboroads/laravel-tinker/compare/v2.5.1...v2.6.0
[2.5.1]: https://github.com/Roboroads/laravel-tinker/compare/v2.5.0...v2.5.1
[2.5.0]: https://github.com/Roboroads/laravel-tinker/compare/v2.4.0...v2.5.0
[2.4.0]: https://github.com/Roboroads/laravel-tinker/compare/v2.3.4...v2.4.0
[2.3.4]: https://github.com/Roboroads/laravel-tinker/compare/v2.3.3...v2.3.4
[2.3.3]: https://github.com/Roboroads/laravel-tinker/compare/v2.3.2...v2.3.3
[2.3.2]: https://github.com/Roboroads/laravel-tinker/compare/v2.3.1...v2.3.2
[2.3.1]: https://github.com/Roboroads/laravel-tinker/compare/v2.3.0...v2.3.1
[2.3.0]: https://github.com/Roboroads/laravel-tinker/compare/v2.2.2...v2.3.0
[2.2.2]: https://github.com/Roboroads/laravel-tinker/compare/v2.2.1...v2.2.2
[2.2.1]: https://github.com/Roboroads/laravel-tinker/compare/v2.2.0...v2.2.1
[2.2.0]: https://github.com/Roboroads/laravel-tinker/compare/v2.1.0...v2.2.0
[2.1.0]: https://github.com/Roboroads/laravel-tinker/compare/v2.0.1...v2.1.0
[2.0.1]: https://github.com/Roboroads/laravel-tinker/compare/v2.0.0...v2.0.1
[2.0.0]: https://github.com/Roboroads/laravel-tinker/compare/v1.3.1...v2.0.0
[1.3.1]: https://github.com/Roboroads/laravel-tinker/compare/v1.3.0...v1.3.1
[1.3.0]: https://github.com/Roboroads/laravel-tinker/compare/v1.2.1...v1.3.0
[1.2.1]: https://github.com/Roboroads/laravel-tinker/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/Roboroads/laravel-tinker/compare/v1.1.1...v1.2.0
[1.1.1]: https://github.com/Roboroads/laravel-tinker/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/Roboroads/laravel-tinker/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/Roboroads/laravel-tinker/compare/v0.0.1-ALPHA...v1.0.0
[0.0.1-ALPHA]: https://github.com/Roboroads/laravel-tinker/commits/v0.0.1-ALPHA
