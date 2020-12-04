# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added

### Changed
- Updated dependencies
### Deprecated

### Removed

### Fixed
- Compatibility with 2020.3
### Security
## [1.3.0]
### Added
- Laravel Tinker settings page (under Tools)
- [ISS-30](https://github.com/Roboroads/laravel-tinker/issues/30) Ability to disable start and finish messages in output
- [ISS-21](https://github.com/Roboroads/laravel-tinker/issues/21) Ability to add a shortcut to close all involving Tinker
### Changed
- Dependencies updated to the latest versions
### Fixed
- The title of the tinker screen was editable - now it isn't.
- [ISS-28](https://github.com/Roboroads/laravel-tinker/issues/28) Ending your tinker console with a comment now works correctly again
  - Credits to [@emargareten](https://github.com/emargareten)
 
## [1.2.1]
### Added
- [ISS-25](https://github.com/Roboroads/laravel-tinker/issues/25) When running tinker it will now save all documents before executing
### Fixed
- [ISS-23](https://github.com/Roboroads/laravel-tinker/issues/23) Fixed a bug where output would display twice when the output highlighter didn't find output to highlight.

## [1.2.0]
### Added
- [ISS-7](https://github.com/Roboroads/laravel-tinker/issues/7) Output syntax highlighting

### Fixed
- [ISS-11](https://github.com/Roboroads/laravel-tinker/issues/11) Plugin won't create a null file anymore on windows. 
- [ISS-16](https://github.com/Roboroads/laravel-tinker/issues/16) Only 1 tinker console will be open at all times.
- [ISS-18](https://github.com/Roboroads/laravel-tinker/issues/18) Added the LARAVEL_START constant to the tinker run script.
- [ISS-19](https://github.com/Roboroads/laravel-tinker/issues/19) Colors in output window now reset to IDE colors when running.
- [ISS-12](https://github.com/Roboroads/laravel-tinker/issues/12) & [ISS-20](https://github.com/Roboroads/laravel-tinker/issues/20) Fixes `Class 'Laravel\Tinker\ClassAliasAutoloader' not found`
  - By just checking if it exists before loading it
  - Should fix the plugin for laravel/tinker versions `<= 1.0.1`
   
## [1.1.1]
### Added
- [ISS-10](https://github.com/Roboroads/laravel-tinker/issues/10) Your last command will now be saved (on per-project level)
- Non-obtrusive reminder you can support this package.
 
### Changed
- [ISS-6](https://github.com/Roboroads/laravel-tinker/issues/6) Output window now (hopefully) always inherits the font and color of the IDE

### Fixed
- The link to PHP settings in error dialogs now *acually* links to the correct settings page.
- [ISS-9](https://github.com/Roboroads/laravel-tinker/issues/9) Output showing slow on larger outputs

## [1.1.0]

### Added
- Run button at the opening tag of the tinker console

### Changed
- Cleaned output of tinker output window
- PHP Runner script is now a php file, makes it easier to work with in the IDE.
 
### Fixed
- Default keybinding in Mac OS
- Toolwindow not opening automatically if not opened before
- Leaving zombie processes (something pcntl did)
- Silent crash when docker was not set
- Tinker process not shutting down because of waiting for non-existent input

## [1.0.0]
### Added
- [ISS-1](https://github.com/Roboroads/laravel-tinker/issues/1) A correct way to interact with remote interpreters

### Changed
- Made errors more to the point
- Better code splitting in message balloons
- More text in the readme
- Better formatting in tinker output
 
### Fixed
- Laravel now actually gets bootstrapped
- Removed needless output from interpreter, like docker startup messages
- Made output toolwindow font show in readable colors
- Campatibility issues that arose in 0.0.1-ALPHA
 
 
## [0.0.1-ALPHA]
### Added
- Everything
