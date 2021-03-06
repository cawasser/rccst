<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Possible Refactoring](#possible-refactoring)
  - [Data Source Metadata](#data-source-metadata)
    - [Semantic](#semantic)
    - [Atoms](#atoms)
    - [Molecules (composed of Atoms)](#molecules-composed-of-atoms)
    - [Collections (of Molecules)](#collections-of-molecules)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Possible Refactoring

- _clj_
  - _rccst_
    - core.clj
    - defaults.clj
    - system.clj
    - version.clj
    - _component_
      - channel.clj _(new)_ 
      - database.clj
      - data_source.clj _(new)_
      - pub_sub.clj
      - repl.clj
      - webserver.clj
      - websocket.clj
    - _routes_
      - csrf.clj
      - dev_mode.clj
      - prod_mode.clj
      - render.clj
      - routes.clj _(rename to api.clj?)_
      
- _cljs_
  - _rccst_
    - core.cljs
    - db.clj
    - events.cljs
    - subs.cljs
  - _subscriptions_
    - handlers.cljs _(formerly subscription_handlers.cljs)_
  - _views_
    - _catalog_
      - atoms.cljs
      - molecules.cljs
      - templates.cljs
      - services.cljs
      - technologies.cljs
      - giants.cljs
    - _giants_
    - _services_
    - _technologies_
      - clojure.cljs
      - clojurescript.cljs
      - system-services.cljs
  - _ui-component_
    - _atom_
      - _input_
        - text.cljs
        - text-area.cljs
        - color-rgba.cljs
        - date.cljs
        - time.cljs
        - button.cljs
      - _layout_
        - grid.cljs
        - multi-column.cljs
        - text-flow.cljs
        - h-box.cljs
        - v-box.cljs
      - _chart_
        - line.cljs
        - bar.cljs
        - pie.cljs
        - area.cljs
        - radar.cljs
        - funnel.cljs
        - sankey.cljs
      - _card_
        - simple.cljs
        - flippable.cljs
      - _misc_
      - _globe_
        - globe.cljs
      - _table_
      - _text_
        - markdown.cljs
      - _diagram_
      - _example_
    - _molecule_
      - _labeled-input_
        - text.cljs
        - text-area.cljs
        - color-rgba.cljs
      - tabbed-panel.cljs
      - side-bar.cljs
      - dnd-pallet.cljs
      - _example_
    - _template_
      - catalog.cljs
      - login.cljs
      - widget.cljs
      - data-source-dnd.cljs




## Data Source Metadata

### Semantic

- identifier
  - system
  - domain
- name
- description
- attribute
- location
- direction
- measurement
- setting
- comment
- event (?)
- when

### Atoms

- text
- number (float/double/integer/long/etc.)
- guid/uuid ([`#uuid`](https://clojure.org/reference/reader#_built_in_tagged_literals))
- date-time ([`#inst`](https://clojure.org/reference/reader#_built_in_tagged_literals))

### Molecules (composed of Atoms)

- velocity (speed & direction)
- geo-ref (lat, lon, alt)
- ephemeris (position, orbit)
- phone-number (county code, area, exchange, etc.)
- address (omg...)
  - shipping
  - billing
  - residence

### Collections (of Molecules)

- row-col
- graph
  - undirected
  - directed
  - directed-acyclic
  - weighted
- stream



