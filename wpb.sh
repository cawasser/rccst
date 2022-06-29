#!/bin/bash

#!/usr/bin/env bash
set -e

if [[ $# -lt 1 ]] || [[ $# -gt 2 ]]
then
    echo "Usage: wpb environment [package]"
    echo "where:"
    echo "   environment = dev or prod"
    echo "   package = if specified, script will use npm to install the package"
    exit 1
elif [ $1 != "dev" ] && [ $1 != "prod" ]
then
    echo "Usage: wpb environment [package]"
    echo "where:"
    echo "   environment = dev or prod"
    echo "   package = if specified, script will use npm to install the package"
    exit 1

fi

#
# If we provided a package, use npm to install it
#
if [ $# -eq 2 ]
then
  echo "==>> Using npm to install package:" $2
  npm install $2
else
  echo "===>> Using npm to update packages"
  npm install
fi

echo "==>> Compiling ClojureScript with shadow-cljs"
npx shadow-cljs compile app

configFile="webpack-${1}-config.js"
echo "==>> Running webpack using the" $configFile "configuration file"
npx webpack -c $configFile
