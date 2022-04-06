#Webpack

This page details our use of webpack to package javascript files.
Jira issue 239 is the basis of this, however other issues may be
resolved by this.

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

Run the webpack bundler from the command line.  This will injest the shadow-created requires.js and
produce a file we call bundle.js.

Make sure to include the bundle.js as a script file in index.html BEFORE our app.js file.

##Required Packages

###Webpack

###React
