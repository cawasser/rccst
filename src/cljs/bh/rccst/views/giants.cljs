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
  [layout/page {:extra-classes :rccst}
   [flex/flex-panel {:height "calc(100vh - 2rem)"}
    [flex/flex-top
     [layout/page-header {:extra-classes :rccst}
      [layout/page-title "'Giants'"]
      [layout/markdown-block "It has been said that [\"we stand on the shoulders of giants\"](https://www.phrases.org.uk/meanings/268025.html).
      The following people may not all qualify as 'giants', but we certainly have
      looked to them for insight, inspiration, guidance, and general thoughtfulness."]
      [layout/section]]]

    [columns people 4]]])



