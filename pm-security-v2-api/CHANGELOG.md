# Change Log - EMI-HELPER-API
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).


##  [1.0.0] - 2024-01-19

### Added

* We are using Spring Data Rest
* Added: RestConfig
    * Implements RepositoryRestConfigurer
    * Main configuration for Spring Data Rest
        * Expose id's for all entities
        * Disable POST, PATCH and DELETE for all entities
* Added: Manual entity
* Added: application.properties
    * Port: 9090
    * Database: emi-helper
    * Hibernate: validate
