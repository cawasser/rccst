(ns bh.rccst.views.giants
  (:require [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [re-com.core :as rc]

            [bh.rccst.ui-component.atom.card.flippable-card :as flippable]))


(def image-style {:width        "170px" :height "170px"
                  :display      :block
                  :margin-left  :auto
                  :margin-right :auto})


(def people [{:name  "Rich Hickey"
              :image "imgs/giants/rich-hickey.jpeg"
              :description
              "Discuss various works and presentations

[Are We There Yet?]() (2009)

[Simple Made Easy]() (2011)
              "}

             {:name        "Stu Halloway"
              :image       "imgs/giants/stu-halloway.jpeg"
              :description "Stu is a Principle at Cognitect and talks on many subjects.

[Simplicity Ain't Easy](https://www.youtube.com/watch?v=cidchWg74Y4) (2012)

[The Impedance Mismatch is Our Fault](https://www.infoq.com/presentations/Impedance-Mismatch/) (2012)

[Clojure in 10 Big Ideas](https://www.youtube.com/watch?v=noiGVQoyYHw) (2013)"}

             {:name        "David Nolen"
              :image       "imgs/giants/david-nolen.png"
              :description "David has been the lead developer of [Clojurescript]() for many years.

[ClojureScript: Lisp's Revenge](https://www.youtube.com/watch?v=MTawgp3SKy8) (2013)

[ClojureScript in the Age of TypeScript](https://www.youtube.com/watch?v=3HxVMGaiZbc) (2021)"}

             {:name        "Dan Holmsand"
              :image       "imgs/giants/dan-holmsand.jpeg"
              :description "Dan started [Reagent](https://reagent-project.github.io)"}

             {:name        "Bret Victor"
              :image       "imgs/giants/bret-victor.jpg"
              :description "Bret has worked at Magic Ink and Apple, focusing on human-centric design.

[Drawing Dynamic Visualizations](http://worrydream.com/#!/DrawingDynamicVisualizationsTalk) (2013)

[Media for the Unthinkable](http://worrydream.com/#!/MediaForThinkingTheUnthinkable) (2013)"}

             {:name        "James Reeves"
              :image       "imgs/giants/james-reeves.jpg"
              :description "James is the developer of [Compojure](https://github.com/weavejester/compojure) and [Hiccup](https://github.com/weavejester/hiccup) and many others.

[Transparency through data](https://www.youtube.com/watch?v=zznwKCifC1A) (2017)"}

             {:name        "Dave Martin"
              :image       "imgs/giants/dave-martin.jpeg"
              :description "Dave started the development of [Willa](https://github.com/DaveWM/willa), a data-driven way to develop Kafka Topologies.

[Kafka Streams, the Clojure way](https://blog.davemartin.me/posts/kafka-streams-the-clojure-way/) (2019)"}

             {:name        "Aysylu Greenberg"
              :image       "imgs/giants/aysylu-greenberg.jpeg"
              :description "Aysylu developed [Loom](https://github.com/aysylu/loom), a library for working with 'graph data' in Clojure.

[Loom and Graphs in Clojure](https://www.youtube.com/watch?v=wEEutxTYQQU) (2014)

[+ Loom years 2](https://www.youtube.com/watch?v=eadPwx-bVS8) (2016)"}

             {:name        "Stuart Sierra"
              :image       "imgs/giants/stuart-sierra.jpg"
              :description "Stuart is the developer of [Component](https://github.com/stuartsierra/component), a Clojure library for working with stateful-infrastructure within an application.

[Components Just Enough Structure](https://www.youtube.com/watch?v=13cmHf_kt-Q) (2014)]"}

             {:name        "Mark Nutter"
              :image       "imgs/giants/mark-nutter.jpeg"
              :description "Mark developed [Woolybear](https://github.com/manutter51/woolybear) as an experiment in Atomic Design for Clojurescript UIs.

[Re-usable GUI Components with Re-frame and Atomic Design](https://www.youtube.com/watch?v=JCY_cHzklRs) (2018)"}

             {:name        "Sean Corfield"
              :image       "imgs/giants/sean-corfield.jpeg"
              :description "Sean is a professional Clojure developer. He maintains a number of OSS libraries for the Clojure community
              including [next.jdbc](https://github.com/seancorfield/next-jdbc).

[Real World Clojure Doing Boring Stuff With An Exciting Language](https://www.youtube.com/watch?v=75U3W8Y2zzw) (2020)

[REPL Driven Development, Clojure's Superpower ](https://www.youtube.com/watch?v=gIoadGfm5T8) (2021)"}

             {:name        "Mark Bastian"
              :image       "imgs/giants/mark-bastian.jpg"
              :description "Mark is a software developer and has presented at several Clojure conferences.

[Bottom Up vs Top Down Design in Clojure](https://www.youtube.com/watch?v=Tb823aqgX_0) (2015)

[Defeating the Four Horsemen of the Coding Apocalypse](https://www.youtube.com/watch?v=jh4hMAvygjk) (2019)]"}

             {:name        "Mark Engelberg"
              :image       "imgs/giants/mark-engleberg.jpg"
              :description "Mark works with his son Alex, and the two have developed a number of useful tools
              for the Clojure community, including [rolling-stones]() and [loco](https://github.com/aengelberg/loco).

[Solving Problems with Automata](https://www.youtube.com/watch?v=AEhULv4ruL4) (2015)]

[Solving Problems Declaratively](https://www.youtube.com/watch?v=TA9DBG8x-ys) (2017)"}

             {:name        "Alex Engelberg"
              :image       "imgs/giants/alex-engelberg.jpg"
              :description "Alex works with his father Mark, and the two have developed a number of useful tools
              for the Clojure community, including [rolling-stones]() and [loco](https://github.com/aengelberg/loco).

[Solving Problems with Automata](https://www.youtube.com/watch?v=AEhULv4ruL4) (2015)]"}

             {:name        "Martin Kleppmann"
              :image       "imgs/giants/martin-kleppmann.jpg"
              :description "Martin is one of the original developers of [Kafka](https://kafka.apache.org), based upon his work with
              [Apache Samza](https://samza.apache.org), while at Linkedin.

[Turning the database inside out with Apache Samza](https://www.youtube.com/watch?v=fU9hR3kiOK0) (2014)

[Transactions: myths, surprises and opportunities](https://www.youtube.com/watch?v=5ZjhNTM8XU8) (2015)"}

             {:name        "Thomas Heller"
              :image       "imgs/giants/thomas-heller.jpeg"
              :description "Thomas is the developer and maintainer of the [Shadow-Cljs](https://github.com/thheller/shadow-cljs) build tool.

[shadow-cljs with Thomas Heller (podcast)](https://www.listennotes.com/podcasts/clojurestream/s3-e3-shadow-cljs-with-hE6kaKgzCSJ/) (2020)"}

             {:name        "Mike Thompson"
              :image       ""
              :description "Mike is the \"face\" of [Re-frame](https://github.com/Day8/re-frame) his employer, Day8
              also build the [Re-com](https://github.com/Day8/re-com) UI library.

[re-frame with Mike Thompson (podcast)](https://www.listennotes.com/podcasts/clojurestream/s4-e3-re-frame-with-mike-kw5eNt0HWAh/) (2020)"}

             {:name        "Eric Normand"
              :image       "imgs/giants/eric-normand.jpeg"
              :description "Eric is a Clojure trainer, writer, and lecturer. He runs [ericnormand.me](https://ericnormand.me).

[Building composable abstractions](https://www.youtube.com/watch?v=jJIUoaIvD20) (2016)

[All I needed for FP I learned in High School Algebra](https://www.youtube.com/watch?v=epT1xgxSpFU) (2017)"}

             {:name        "Will Byrd"
              :image       "imgs/giants/wil-byrd.png"
              :description "Wil is a programming language researcher, and co-author of the [Reasoned Schemer](https://mitpress.mit.edu/books/reasoned-schemer) and
              [miniKanren](http://minikanren.org).

[Barliman: trying the halting problem backwards, blindfolded](https://www.youtube.com/watch?v=er_lLvkklsk) (2016)

[The Most Beautiful Program Ever Written](https://www.youtube.com/watch?v=OyfBQmvr2Hc) (2017)"}

             {:name        "Paul deGrandis"
              :image       "imgs/giants/paul-degrandis.jpeg"
              :description "Paul has worked for Cognitect and developed a data-driven approach to UI develop while
              consulting for Consumer Reports.

[Unlocking data-driven systems](https://www.youtube.com/watch?v=BNkYYYyfF48) (2014)"}])


(defn- personality
  "Returns a 'flippable' card for a 'person' to be presented on the 'Giants' page. Uses the
  [react-ui-cards](https://github.com/nukeop/react-ui-cards) react component library, specifically
  the [flipping card](https://github.com/nukeop/react-ui-cards#flipping-card)
  variant (there are several provided by the library).

  ---

  - name : (string) the person's name
  - description : (markdown string) whatever you want to say about this person, including things like hyperlinks
  - image : (filepath) [optional] name of the file to show as an image (defaults to imgs/hammer-icon-16x16.png)
  "
  [name description & [image]]
  (log/info "personality" image)
  [flippable/card
   :style {:width 270 :height 400}
   :front [:div {:style flippable/default-style}
           [rc/v-box :src (rc/at)
            :gap "10px"
            :align :center
            :justify :center
            :children [[:figure.image
                        [:img.is-rounded {:src (or (if (empty? image) nil image) "imgs/hammer-icon-16x16.png")}]]
                       [:p.title.is-4.has-text-centered name]]]]
   :back [:div {:style flippable/default-style}
          [layout/section
           [layout/markdown-block description]]]])


(defn columns
  "Builds a react 'fragment' of Bulma [columns](), which have some number of
  [column]() `div`s that each hold a ([[personality]]). Determines how many
  `column` in each `columns` via the `row` parameter. Adds rows of columns
  as needed.

  ___

  - people : (vector of entities) collection of hash-maps that define each person's data (see ([[personality]]))
  - row : (integer) number of columns/cards ro put in each row.
  "
  [people row]
  (into [:<>]
    (for [p (partition-all row people)]
      (into [:div.columns.is-1]
        (for [{:keys [name description image]} p]
          [:div.column
           [personality name description image]])))))


(defn view []
  [layout/page {:extra-classes :is-fluid}
   [flex/flex-panel {:height "90vh"}
    [flex/flex-top
     [layout/page-header {:extra-classes :is-fluid}
      [layout/page-title "'Giants'"]
      [layout/markdown-block "It has been said that [\"we stand on the shoulders of giants\"](https://www.phrases.org.uk/meanings/268025.html).
      The following people may not all qualify as 'giants', but we certainly have
      looked to them for insight, inspiration, guidance, and general thoughtfulness."]
      [layout/section]]]

    [columns people 5]]])



