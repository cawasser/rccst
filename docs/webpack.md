#Webpack

This page details our use of webpack to package javascript files.
Jira issue 239 is the basis of this, however other issues may be
resolved by this.

##Instructions for Use
###First Time
Step 1: Clone or pull latest from repo.

Step 2: Open a command window and navigate to your project root.

Step 3: Execute the following commands to install packages you will need:
            
    npm install --save-dev webpack webpack-cli copy-webpack-plugin imports-loader
            
    npm install @astrouxds/react

Step 4: Execute the following command to have shadow-cljs create a file we need:
            
    npx shadow-cljs compile app

Step 5: Execute the following command to have webpack create a file we need:
            
    npx webpack -c webpack-dev-config.js

Step 6: Perform work normally (launch server, launch shadow watch, launch browser, etc)

###Subsequent Use
There are only two cases in which you will need to run steps 4 and 5 above after the initial setup:

- To switch between dev and production modes, perform step 5 above using the correct webpack config file.
- When you've installed a new package via npm for use in your code you'll need to rerun steps 4 and 5.

Once you start using webpack, you must ALWAYS use it, even if the package you download would work perfectly well with Clojurescript.

NOTE: The package you download may require updates to the webpack config files.
      That is beyond the scope of this guide.
      If you need to install additional webpack packages, please update step 3 above.

##Background

A problem occurred when we attempted to add AstroUXD components to rccst.
We ran a spike project to isolate things to just react and Astro.
Still had problems.

Doing some research, we found that shadow-cljs doesn't work with some 
javascript modules. The recommended solution was to use a bundler to 
handle the javascript modules while shadow handled the cljs files.  Webpack
was chosen to handle the bundling.  using that, I was able to get Astro
running in the spike project.

When I attempted to use webpack in rccst, we ran into a problem with the
cesium project. Trial and error and a lot of google-fu helped us resolve that.

##Basic Solution

Modify shadow-cljs.edn to let Shadow know that an external javascript bundler will be used.
Shadow will generate a js file (we call it requires.js) containing a require statement for the 
node-modules we are using.

Run the webpack bundler from the command line.  This will ingest the shadow-created requires.js and
produce a file we call bundle.js.

Make sure to include the bundle.js as a script file in index.html BEFORE our app.js file.

##Required Packages

###Webpack
webpack, webpack-cli, copy-webpack-plugin, imports-loader

##Additional work
Some additional cleanup work and error resolution was performed along with implementing webpack:

- Removed the hand-copied cesium assets since webpack handles that now.
- Removed the assignment of Ion defaultaccesskey from index.html since its handled in code now.
- Removed the assignment of CESIUM_BASE_URL from index.html since its handled in webpack config files.
- Changed location of widgets.css in link tag in index.html.

##Links
I found the following links to be helpful while delving into this.
They are in no particular order:

[](https://code.thheller.com/blog/shadow-cljs/2020/05/08/how-about-webpack-now.html#option-2-js-provider-external)

https://webpack.js.org/guides/getting-started/

https://webpack.js.org/concepts/

https://marko.euptera.com/posts/ionic-clojure-todo-example.html

https://stackoverflow.com/questions/64557638/how-to-polyfill-node-core-modules-in-webpack-5

https://cesium.com/learn/cesiumjs-learn/cesiumjs-webpack/#add-cesiumjs-to-a-webpack-app

https://github.com/CesiumGS/cesium-webpack-example/blob/main/TUTORIAL.md

https://github.com/srothst1/cesiumjs-webpack-starter-tutorial/blob/master/webpack.config.js

https://cesium.com/blog/2016/01/26/cesium-and-webpack/#ive-already-got-webpack-set-up-just-tell-me-how-to-use-cesium

https://github.com/mmacaula/cesium-webpack

https://github.com/CesiumGS/cesium-webpack-example

https://codeburst.io/use-webpack-with-dirname-correctly-4cad3b265a92

https://webpack.js.org/guides/development/


