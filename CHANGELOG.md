# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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
 - A correct way to interact with remote interpreters

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
 
 
## [0.0.1-ALPHA] - 2020-08-27
### Added
 - Everything