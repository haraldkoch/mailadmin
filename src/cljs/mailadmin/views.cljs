(ns mailadmin.views
  (:require [mailadmin.misc :refer [render-table fmt-date-recent fmt-date]]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [ajax.core :refer [GET POST]]
            ))

(defn reset-form-data [data-atom]
  (reset! data-atom (into {} (for [[k _] @data-atom] [k nil]))))

(defn input-field [param data-atom]
  [:input.form-control
   {:type        :text :value (get @data-atom param)
    :placeholder (name param)
    :on-change   #(swap! data-atom assoc param (.-value (.-target %)))}])

(defn add-domain-form [form-data]
  (fn []
    [:div.row
     [:div.col-sm-12


      [:div.input-group
       [input-field :domain form-data]
       [:button.btn.btn-primary
        {:on-click #(do
                     (dispatch [:add-domain @form-data])
                     (reset-form-data form-data))}
        "add domain"]]]]))

(defn domain-input [{:keys [title on-save on-stop]}]
  (let [val (r/atom title)
        stop #(do (reset! val "")
                  (if on-stop (on-stop)))
        save #(let [v (-> @val str clojure.string/trim)]
               (if-not (empty? v) (on-save v))
               (stop))]
    (fn [props]
      [:input (merge props
                     {:type        "text" :value @val :on-blur save
                      :on-change   #(reset! val (-> % .-target .-value))
                      :on-key-down #(case (.-which %)
                                     13 (save)
                                     27 (stop)
                                     nil)})])))

(def domain-edit (with-meta domain-input
                          {:component-did-mount #(.focus (r/dom-node %))}))

(defn domain-item []
  (let [editing (r/atom false)]
    (fn [domain]
      [:li {:class (str (if @editing "editing"))}
       [:div.view
        [:label {:on-double-click #(reset! editing true)} (:domain domain)]
        [:button.destroy {:on-click #(dispatch [:delete-domain domain])}]]
       (when @editing
         [domain-edit {:class   "edit"
                       :title   (:domain domain)
                       :on-save #(dispatch [:update-domain (:id domain) %])
                       :on-stop #(reset! editing false)}])])))

(defn add-domain [domain]
  (dispatch [:add-domain {:domain domain}]))

(defn domains []
  (let [domains (subscribe [:domains])
        domains-loaded? (subscribe [:domains-loaded?])
        form-data (r/atom {:domain nil})]
    (fn []
      (if-not @domains-loaded?
        [:div.row [:div.col-sm-12 "Loading domains..."]]
        [:div.row
         [:div.col-sm-12
          [:h2 "Domains"]
          [domain-input {:id "new-domain"
                         :placeholder "Add a new domain name"
                         :on-save add-domain}]
          [:ul#domain-list
           (for [domain @domains]
             ^{:key (:id domain)} [domain-item domain])]]]))))

(defn status-bar []
  (let [status (subscribe [:status])]
    (fn []
      (if @status
        [:div.row
         [:div.col-sm-12
          [:div.alert.alert-success "Status:" @status]]]))))

(defn error-bar []
  (let [error (subscribe [:error])]
    (fn []
      (if @error
        [:div.row
         [:div.col-sm-12
          [:div.alert.alert-danger "Error:" @error]]]))))


(defn mailadmin-page []
  [:div.container
   [status-bar]
   [error-bar]
   [domains]])