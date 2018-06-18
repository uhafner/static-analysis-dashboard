# Static Analysis Dashboard

This dashboard visualizes results of various static code analysis tools - it understands the result
files of almost 100 different static code analysis tools. You can upload reports of different tools 
and visualize the distribution of the issues by severity, category, etc. It is also possible to upload
a sequence of reports of the same type to show the trend of the reports.

You can view this tool as a stand alone version of the corresponding static analysis suite in the
continuous integration server Jenkins. Basically this Spring Boot based dashboard uses the 
same modules to analyze reports and visualize the results. For more details on Jenkins static analysis 
suite please refer to the documentation in [Jenkins wiki](https://wiki.jenkins.io/x/CwDgAQ). For technical 
details on how to use the static analysis model (i.e. the API to read and parse reports) please refer to the
[Analysis Model GitHub page](https://github.com/jenkinsci/analysis-model).       
             
All source code is licensed under the MIT license.

This dashboard can be either started locally using the integrated Tomcat container of the Spring Boot application 
or [![deployed to Heroku](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy). 

[![Travis](https://img.shields.io/travis/uhafner/static-analysis-dashboard.svg)](https://travis-ci.org/uhafner/static-analysis-dashboard)
[![Codecov](https://img.shields.io/codecov/c/github/uhafner/static-analysis-dashboard.svg)](https://codecov.io/gh/uhafner/static-analysis-dashboard)
[![GitHub pull requests](https://img.shields.io/github/issues-pr/uhafner/static-analysis-dashboard.svg)](https://github.com/uhafner/static-analysis-dashboard/pulls)
![JDK8](https://img.shields.io/badge/jdk-8-yellow.svg)
[![License: MIT](https://img.shields.io/badge/license-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Running Locally

Make sure you have Java and Maven installed.  Also, install the [Heroku CLI](https://cli.heroku.com/).

```sh
$ mvn install
$ heroku local:start
```

Your app should now be running on [localhost:5000](http://localhost:5000/).

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku master
$ heroku open
```

