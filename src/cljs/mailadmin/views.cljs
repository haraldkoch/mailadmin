(ns mailadmin.views
  (:require [mailadmin.misc :refer [render-table fmt-date-recent fmt-date]]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [ajax.core :refer [GET POST]]))

(defn input-field [{:keys [initial on-save on-stop]}]
  (let [val (r/atom initial)
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

(def edit-field (with-meta input-field
                           {:component-did-mount #(.focus (r/dom-node %))}))

(defn domain-item []
  (let [editing (r/atom false)]
    (fn [{:keys [:id :domain]}]
      [:li {:class (str (if @editing "editing"))}
       [:div.view
        [:label {:on-double-click #(reset! editing true)} domain]
        [:button.destroy {:on-click #(dispatch [:delete-domain id])}]]
       (when @editing
         [edit-field {:class   "edit"
                      :initial domain
                      :on-save #(dispatch [:update-domain id %])
                      :on-stop #(reset! editing false)}])])))

(defn domains []
  (let [domains (subscribe [:domains])
        domains-loaded? (subscribe [:domains-loaded?])]
    (fn []
      (if-not @domains-loaded?
        [:div.row [:div.col-sm-12 "Loading domains..."]]
        [:div.row
         [:div.col-sm-12
          [:h2 "Domains"]
          [input-field {:id          "new-domain"
                        :placeholder "Add a new domain name"
                        :on-save     #(dispatch [:add-domain {:domain %}])}]
          [:ul#domain-list
           (for [domain @domains]
             ^{:key (:id domain)} [domain-item domain])]]]))))

(defn input-forwarding [{:keys [values on-save on-stop]}]
  (let [val (r/atom values)
        stop #(do (reset! val {})
                  (if on-stop (on-stop)))
        save #(let [v @val]
               (if-not (empty? v) (on-save v))
               (stop))]
    (fn [props]
      [:div
       [:label "Source: "]
       [:input {:type        "text" :value (:source @val)
                :on-change   #(swap! val assoc :source (-> % .-target .-value))
                :on-key-down #(case (.-which %)
                               13 (save)
                               27 (stop)
                               nil)}]
       [:label "Dest: "]
       [:input {:type        "text" :value (:destination @val)
                :on-change   #(swap! val assoc :destination (-> % .-target .-value))
                :on-key-down #(case (.-which %)
                               13 (save)
                               27 (stop)
                               nil)}]
       [:button.btn.btn-primary {:on-click #(save)} "Save"]])))

(defn forwarding-item []
  (let [editing (r/atom false)]
    (fn [{:keys [:id :source :destination]}]
      [:li {:class (str (if @editing "editing"))}
       [:div.view
        [:label {:on-double-click #(reset! editing true)} source]
        [:spam " -> "]
        [:label {:on-double-click #(reset! editing true)} destination]
        [:button.destroy {:on-click #(dispatch [:delete-forwarding id])}]]
       (when @editing
         [input-forwarding {:class   "edit"
                            :values  {:source source :destination destination}
                            :on-save #(dispatch [:update-forwarding id %])
                            :on-stop #(reset! editing false)}])])))

(defn forwardings []
  (let [forwardings (subscribe [:forwardings])
        forwardings-loaded? (subscribe [:forwardings-loaded?])]
    (fn []
      (if-not @forwardings-loaded?
        [:div.row [:div.col-sm-12 "Loading forwardings..."]]
        [:div.row
         [:div.col-sm-12
          [:h2 "Aliases"]
          [input-forwarding {:id          "new-forwarding"  ;; FIXME
                             :values      {}
                             :placeholder "Add a new forwarding"
                             :on-save     #(dispatch [:add-forwarding %])}]
          [:ul#forwarding-list
           (for [forwarding @forwardings]
             ^{:key (:id forwarding)} [forwarding-item forwarding])]]]))))

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
   [domains]
   [forwardings]])