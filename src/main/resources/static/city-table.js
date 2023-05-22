class CityTable extends HTMLElement {
  constructor() {
    super();
    let that = this;
    this.formSelector = '#cityTable'

    this.shad = this.attachShadow({mode: "open"});
    this.shadowRoot.innerHTML =
      `<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">

      <div id="cityTable">
         <div class="d-flex flex-column">
           <span id='label' class="font-bold text-gray-700"></span>
           <table class="table table-sm" id="table">
             <thead>
               <tr class="text-sm">
               </tr>
             </thead>
             <tbody>
             </tbody>
           </table>
           <div class="d-flex flex-column">
             <slot>
           </div>
         </div>
      </div>`;
  }

  /**
   * Runs each time the element is appended to or moved in the DOM
   */
  connectedCallback() {
  }

  /**
   * Runs when the element is removed from the DOM
   */
  disconnectedCallback() {
  }

  /**
   * Create a list of attributes to observe
   */
  static get observedAttributes() {
    return ['label', 'values', 'headers'];
  }

  /**
   * Runs when the value of an attribute is changed on the component
   * @param  {String} name     The attribute name
   * @param  {String} oldValue The old attribute value
   * @param  {String} newValue The new attribute value
   */
  attributeChangedCallback(name, oldValue, newValue) {
    switch (name){
      case 'values':
        if(newValue){
          let tbody = this.shadowRoot.querySelector(this.formSelector+" #table tbody");
          this.addData(tbody, JSON.parse(newValue));
        }
        break;
      case 'headers':
        let thead = this.shadowRoot.querySelector(this.formSelector+" #table thead tr");
        this.addHeaders(thead, JSON.parse(newValue));
        break;
      case 'label':
        let label = this.shadowRoot.querySelector(this.formSelector+" #label");
        label.textContent = newValue;
        break;
    }
  }

  addHeaders(thead, headers) {
    $(thead).empty();
    this.headers = headers;
    headers.forEach((header) => {
      $(thead).append(`<th scope="col" class="border-top-0">${header.name}</th>`);
    });
  }

  addData(tbody, data) {
    $(tbody).empty();
    data.forEach((item) => {
      const tr = $(`<tr data-item='${item[this.headers[0].field]}'></tr>`).click(()=>{this.handleClick(item)});
      this.headers.forEach((header) => {
        tr.append(`<td>${item[header.field]}</td>`);
      });
      $(tbody).append(tr);
    });
  }

  handleClick(zone){
    let callback = this.getAttribute('onChange');
    var fn = window[callback];
    if (typeof fn === "function") {
      fn(zone.workZone);
    }

    return false;
  }
}

if ('customElements' in window) {
  customElements.define('city-table', CityTable);
}
