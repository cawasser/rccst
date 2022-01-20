(ns bh.rccst.views.giants
  (:require [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [re-com.core :as rc]

            [bh.rccst.ui-component.flippable-card :as flippable]))


(def image-style {:width        "170px" :height "170px"
                  :display      :block
                  :margin-left  :auto
                  :margin-right :auto})


(def people [{:name  "Rich Hickey"
              :image "imgs/rich-hickey.jpeg"
              :description
              "Discuss various works and presentations

[Are We There Yet?]() (2009)

[Simple Made Easy]() (2011)
              "}

             {:name        "Stu Halloway"
              :image       "imgs/stu-halloway.jpeg"
              :description "Discuss the various works and presentations"}

             {:name        "David Nolen"
              :image       "imgs/david-nolen.png"
              :description "Discuss the various works and presentations"}

             {:name        "Dan Holmsand"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Bret Victor"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "James Reeves"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Dave Martin"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Stuart Sierra"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Mark Nutter"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Sean Corfield"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Mark Bastian"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Alex Engelberg"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Martin Kleppmann"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Thomas Heller"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Eric Normand"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Will Byrd"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Paul deGrandis"
              :image       ""
              :description "Discuss the various works and presentations"}])


(defn- personality [name description & [image]]
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


(defn columns [people row]
  (for [p (partition-all row people)]
    (into [:div.columns.is-1]
      (for [{:keys [name description image]} p]
        [:div.column
         [personality name description image]]))))


(defn view []
  [layout/page {:extra-classes :rccst}
   [flex/flex-panel {:height "calc(100vh - 2rem)"}
    [flex/flex-top
     [layout/page-header {:extra-classes :rccst}
      [layout/page-title "'Giants'"]
      [layout/markdown-block "It has been said that [\"we stand on the shoulders of giants\"](https://www.phrases.org.uk/meanings/268025.html).
      The following people may not all qualify as 'giants', but we certainly have
      looked to them for insight, inspiration, guidance, and general thoughtfulness."]
      [layout/section]]]

    (into [:<>] (columns people 4))]])



