(ns bh.rccst.ui-component.markdown
  (:require ["react-markdown" :as ReactMarkdown]))


(defn markdown [content]
  [:> ReactMarkdown {:source content}])