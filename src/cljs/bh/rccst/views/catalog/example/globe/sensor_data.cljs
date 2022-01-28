(ns bh.rccst.views.catalog.example.globe.sensor-data
  (:require [reagent.core :as reagent]
            [bh.rccst.views.catalog.example.globe.raw-sensor-data :as raw-sensors]
            ["@fortawesome/free-solid-svg-icons" :refer (faCloudSunRain, faUmbrella,)]))


(def sensor-data '({:time        0,
                    :cell        [0 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [0 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [0 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [0 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [0 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [0 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [0 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [0 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [0 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [0 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [1 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [1 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [1 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [1 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [1 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [1 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [1 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [1 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [1 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "goes-west", :sensor "abi-meso-11"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [1 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [2 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [2 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [2 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [2 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [2 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [2 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [2 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [2 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [2 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [2 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [3 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [3 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [3 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [3 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [3 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [3 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [3 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [3 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [3 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [3 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [4 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [4 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [4 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [4 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [4 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [4 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [4 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [4 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [4 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [4 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [5 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [5 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [5 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [5 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}
                                   {:platform "goes-west", :sensor "abi-meso-4"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [5 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [5 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [5 6],
                    :coverage    #{{:platform "goes-east", :sensor "abi-meso-10"}
                                   {:platform "goes-west", :sensor "abi-3"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [5 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [5 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [5 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [6 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [6 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [6 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [6 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [6 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [6 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [6 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [6 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [6 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [6 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [7 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [7 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [7 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [7 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [7 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [7 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [7 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [7 7],
                    :coverage    #{{:platform "goes-east", :sensor "abi-meso-2"}
                                   {:platform "goes-west", :sensor "abi-3"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [7 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [7 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [8 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [8 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [8 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [8 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [8 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [8 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [8 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [8 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [8 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [8 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [9 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [9 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [9 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [9 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [9 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [9 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [9 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [9 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [9 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        0,
                    :cell        [9 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [0 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [0 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [0 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [0 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [0 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [0 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [0 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [0 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [0 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [0 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [1 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [1 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [1 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [1 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [1 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [1 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [1 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [1 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [1 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [1 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [2 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [2 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [2 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [2 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [2 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [2 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [2 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [2 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [2 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [2 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [3 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [3 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [3 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [3 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [3 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [3 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [3 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [3 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [3 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [3 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [4 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [4 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [4 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [4 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [4 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [4 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [4 6],
                    :coverage    #{{:platform "goes-east", :sensor "abi-meso-2"}
                                   {:platform "goes-west", :sensor "abi-3"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [4 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [4 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [4 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [5 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [5 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [5 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [5 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [5 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [5 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [5 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [5 7],
                    :coverage    #{{:platform "goes-east", :sensor "abi-meso-10"}
                                   {:platform "goes-west", :sensor "abi-3"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [5 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [5 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [6 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [6 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [6 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [6 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [6 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [6 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-west", :sensor "abi-meso-11"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [6 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [6 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [6 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [6 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [7 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [7 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [7 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [7 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [7 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [7 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [7 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "goes-east", :sensor "abi-1"}
                                   {:platform "goes-west", :sensor "abi-meso-4"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [7 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [7 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [7 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [8 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [8 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [8 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [8 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"
                                    {:platform "metop-yy", :sensor "avhhr-6"}
                                    {:platform "goes-east", :sensor "abi-1"}},}
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [8 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [8 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [8 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [8 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [8 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [8 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [9 0],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [9 1],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [9 2],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [9 3],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "metop-yy", :sensor "avhhr-6"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [9 4],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}}
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [9 5],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"}
                                   {:platform "noaa-xx", :sensor "viirs-5"}
                                   {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [9 6],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [9 7],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [9 8],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}
                   {:time        1,
                    :cell        [9 9],
                    :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                    :computed_at "2021-08-02T15:16:05.558813"}))


(def all-aoi-data {"alpha-hd"  {:sensor-type "hidef-image"
                                :symbol      [faUmbrella "images/symbols/cloud-rain-solid.png"] ; currently, these DON'T matter
                                :cells#      {[7 7 "hidef-image" 0]
                                              [7 6 "hidef-image" 1]
                                              [7 6 "hidef-image" 2]
                                              [7 5 "hidef-image" 3]}}
                   "bravo-img" {:sensor-type "image"
                                :symbol      [faCloudSunRain "images/symbols/cloud-sun-rain-solid.png"] ; currently, these DON'T matter
                                :cells       #{[7 2 "image" 0]
                                               [7 1 "image" 1]}}
                   "fire-hd"   {:sensor-type "hidef-image"
                                :symbol      [faCloudSunRain "images/symbols/cloud-sun-rain-solid.png"] ; currently, these DON'T matter
                                :cells       #{[5 3 "hidef-image" 0]
                                               [4 3 "hidef-image" 2] [5 3 "hidef-image" 2]
                                               [4 3 "hidef-image" 3] [5 3 "hidef-image" 3]}}
                   "fire-ir"   {:sensor-type "v/ir"
                                :symbol      [faCloudSunRain "images/symbols/cloud-sun-rain-solid.png"] ; currently, these DON'T matter
                                :cells       #{[5 4 "v/ir" 0]
                                               [5 3 "v/ir" 1] [5 4 "v/ir" 1]
                                               [5 4 "v/ir" 2]
                                               [5 4 "v/ir" 3]}}
                   "severe-hd" {:sensor-type "hidef-image"
                                :symbol      [faCloudSunRain "images/symbols/cloud-sun-rain-solid.png"] ; currently, these DON'T matter
                                :cells       #{[5 6 "hidef-image" 0]
                                               [5 7 "hidef-image" 1] [6 5 "hidef-image" 1]
                                               [6 6 "hidef-image" 2]
                                               [5 7 "hidef-image" 3]}}})


(defn diagram-cell [x y]
  {:x (+ 10 (* x 400)) :y (+ 10 (* y 400))})


(def weather-flow-elements [{:id        "viirs-5"
                             :el-type   :node
                             :type      "globe"
                             :data      {:sensor "viirs-5"}
                             ;:dragHandle "drag-handle"
                             :draggable false
                             :position  (diagram-cell 0 1)}

                            {:id        "abi-meso-11"
                             :el-type   :node
                             :type      "globe"
                             :data      {:sensor "abi-meso-11"}
                             ;:dragHandle "drag-handle"
                             :draggable false
                             :position  (diagram-cell 0 0)}

                            {:id        "goes-east"
                             :el-type   :node
                             :type      "platform"
                             :data      {:label "GOES East"
                                         :image "/images/icons/Weather-Satellite-PNG-Clipart.png"}
                             ;:dragHandle "drag-handle"
                             :draggable false
                             :position  (diagram-cell 1 0)}

                            {:id        "central"
                             :el-type   :node
                             :type      "downlink-terminal"
                             :data      {:label "Wallops"
                                         :image "/images/icons/downlink-terminal.png"}
                             :draggable false
                             :position  (diagram-cell 2 0)}

                            {:id        "washington"
                             :el-type   :node
                             :type      "processing-center"
                             :data      {:label "NSOF Suitland"
                                         :image "/images/icons/processing-center.png"}
                             :draggable false
                             :position  (diagram-cell 3 0)}

                            {:id        "noaa-xx"
                             :el-type   :node
                             :type      "platform"
                             :data      {:label "NOAA XX"
                                         :image "/images/icons/Weather-Satellite-PNG-Clipart.png"}
                             :draggable false
                             :position  (diagram-cell 1 1)}

                            {:id        "mountain"
                             :el-type   :node
                             :type      "downlink-terminal"
                             :data      {:label "Svalbaard/McMurdo"
                                         :image "/images/icons/downlink-terminal.png"}
                             :draggable false
                             :position  (diagram-cell 2 1)}

                            ; edges
                            {:id    "11-n" :el-type :edge :source "abi-meso-11" :target "goes-east"
                             :style {:stroke-width 20 :stroke :gray} :animated true}
                            {:id    "v5-n" :el-type :edge :source "viirs-5" :target "noaa-xx"
                             :style {:stroke-width 20 :stroke :gray} :animated true}
                            {:id    "e-c" :el-type :edge :source "goes-east" :target "central"
                             :style {:stroke-width 50 :stroke :orange} :animated true}
                            {:id    "c-w" :el-type :edge :source "central" :target "washington"
                             :style {:stroke-width 25 :stroke "#f00"} :animated true}
                            {:id    "n-m" :el-type :edge :source "noaa-xx" :target "mountain"
                             :style {:stroke-width 30 :stroke :lightgreen} :animated true}
                            {:id    "m-w" :el-type :edge :source "mountain" :target "washington"
                             :style {:stroke-width 5} :animated true}])


(def system-flow-elements [{:id        "vanilla"
                            :el-type   :node
                            :type      "server"
                            :data      {:label "Vanilla"
                                        :image "/images/icons/Cloud-Server.png"}
                            ;:dragHandle "drag-handle"
                            :draggable false
                            :position  (diagram-cell 0 0)}

                           {:id        "sensor-allocations"
                            :el-type   :node
                            :type      "service"
                            :data      {:label "Sensor Allocations"
                                        :image "/images/icons/gears.png"}
                            ;:dragHandle "drag-handle"
                            :draggable false
                            :position  (diagram-cell 1 1)}

                           {:id        "aois-events"
                            :el-type   :node
                            :type      "event-channel"
                            :data      {:label "AoIs Events"
                                        :image "/images/icons/kafka.png"}
                            ;:dragHandle "drag-handle"
                            :draggable false
                            :position  (diagram-cell 0 3)}

                           {:id        "sensor-allocations-view"
                            :el-type   :node
                            :type      "view-channel"
                            :data      {:label "Sensor Allocs View"
                                        :image "/images/icons/kafka.png"}
                            ;:dragHandle "drag-handle"
                            :draggable false
                            :position  (diagram-cell 1 2.5)}

                           {:id        "request-events"
                            :el-type   :node
                            :type      "event-channel"
                            :data      {:label "Comms Request Events"
                                        :image "/images/icons/kafka.png"}
                            ;:dragHandle "drag-handle"
                            :draggable false
                            :position  (diagram-cell 2 3)}

                           {:id        "comms-allocations"
                            :el-type   :node
                            :type      "service"
                            :data      {:label "Comms Allocations"
                                        :image "/images/icons/gears.png"}
                            ;:dragHandle "drag-handle"
                            :draggable false
                            :position  (diagram-cell 2 1)}

                           {:id        "comms-allocations-view"
                            :el-type   :node
                            :type      "view-channel"
                            :data      {:label "Comms Allocs View"
                                        :image "/images/icons/kafka.png"}
                            ;:dragHandle "drag-handle"
                            :draggable false
                            :position  (diagram-cell 3 2.5)}

                           {:id    "v-a" :el-type :edge :source "vanilla" :target "aois-events"
                            :style {:stroke-width 20 :stroke :gray} :animated true}
                           {:id    "a-s" :el-type :edge :source "aois-events" :target "sensor-allocations"
                            :style {:stroke-width 20 :stroke :gray} :animated true}
                           {:id    "s-sv" :el-type :edge :source "sensor-allocations" :target "sensor-allocations-view"
                            :style {:stroke-width 20 :stroke :gray} :animated true}

                           {:id    "v-r" :el-type :edge :source "vanilla" :target "request-events"
                            :style {:stroke-width 20 :stroke :gray} :animated true}
                           {:id    "r-c" :el-type :edge :source "request-events" :target "comms-allocations"
                            :style {:stroke-width 20 :stroke :gray} :animated true}
                           {:id    "c-rv" :el-type :edge :source "comms-allocations" :target "comms-allocations-view"
                            :style {:stroke-width 20 :stroke :gray} :animated true}])


(def all-sensor-data raw-sensors/raw-sensor-data)

