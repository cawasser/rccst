(ns rccst.db)

(def default-db
  {:name             "RCCST"
   :version          ""
   :logged-in?       false
   :pub-sub-started? false

   ; this data comes from the server upon successful login
   ;
   ; provides meta-data about all the data-services the server can provide for subscriptions
   ;
   ; need to know the "name" for the subscription vector (the keys), also provides
   ;
   ;    :source/returns - what kind of data is returned (vector? hash-map? integer?)
   ;    :source/entity-contents - description of the contents of the collection that is returned
   ;
   :avail-sources {:source/coverages {:source/type :source/remote
                                      :source/returns :source/vector-of-entity-meta-coc
                                      :source/entity-contents []}
                   :source/satellites {:source/type :source/remote
                                       :source/returns :source/vector-of-entity-meta-coc
                                       :source/entity-contents []}
                   :source/targets {:source/type :source/remote
                                    :source/returns :source/vector-of-entity-meta-coc
                                    :source/entity-contents []}
                   :source/measurements {:source/type :source/remote
                                         :source/returns :source/vector-of-entity-meta-coc
                                         :source/entity-contents []}}
   :sources          {:number 0
                      :string "empty"}})


