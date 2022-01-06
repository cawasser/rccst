(ns bh.rccst.ui-component.text-input)


(defn input
  "provides a simple text-input field for use in browser UIs

  ---

  - input-type : (keyword) keyword-ized version of anything found [here](https://www.w3schools.com/html/html_form_input_types.asp)
  - placeholder : (string) any placeholder text you want to show the user to guide then on the purpose or data in enter into the field
  - data : (atom) an atom to hold the results

  > NOTE: this ui-component does ***NOT*** performs ny type conversions on the user input. it _always_
  > returns a string. Caller is responsible for any conversion necessary to make sense of the data
  "
  [input-type placeholder data]
  [:div.field
   [:input.input
    {:type        input-type
     :value       @data
     :placeholder placeholder
     :on-change   #(reset! data (-> % .-target .-value))}]])
