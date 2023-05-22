// Extend the HTMLElement class to create the web component
// NOTE: Script with elements should be add to the end to desired html page

class ChooseCity extends HTMLElement {
  constructor() {
    super();
    let that = this;
    this.formSelector = '#chooseCity'

    this.shad = this.attachShadow({mode: "open"});
    this.shadowRoot.innerHTML =
      `<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">

      <form id="chooseCity">
         <div class="d-flex flex-column">
           <span id='header' class="font-bold text-gray-700"></span>
           <span id='label' class="font-bold text-gray-700"></span>
             <div class="d-flex flex-column mb-3">
               <select name="option" id="option-select"></select>
               <div id='errMsg' class="text-danger text-truncate">e</div>
             </div>
           <div class="d-flex flex-column">
             <slot>
           </div>
         </div>
      </form>`;

    this.hideErrorMsg();

    $(this.shadowRoot)
      .find(this.formSelector+" #option-select")
      .change(function (event) {
        that.onSubmitOpt(event);
      })

    this.panelSlot = this.shadowRoot.querySelector("slot");
    this.panelSlot.addEventListener("slotchange", () => this.updateSlot());
  }


  onSubmitOpt(event) {
    const value = $(this.shadowRoot).find(this.formSelector+" #option-select").val();

    if(!value || value == 'Select City'){
      this.addErrorMsg('Select option');
      return;
    } else {
      this.hideErrorMsg();
    }

    let callback = this.getAttribute('onSubmit');
    var fn = window[callback];
    if (typeof fn === "function") {
      fn(value);
    }

    return false;
  }

  updateSlot() {
    var assignedElements = this.panelSlot.assignedElements();

    var that = this
    // for each content panel, create a tab element and place it in the row
    assignedElements.forEach(i => {
      let btSubmit = i.querySelector("button[type=submit]");
      if(btSubmit){
        btSubmit.removeEventListener("click", that.onSubmit)
        btSubmit.addEventListener("click", function (event) {
          that.onSubmit(event);
          event.preventDefault();
          event.stopPropagation();
        });
      }
    })
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
    return ['label', 'header', 'options'];
  }

  /**
   * Runs when the value of an attribute is changed on the component
   * @param  {String} name     The attribute name
   * @param  {String} oldValue The old attribute value
   * @param  {String} newValue The new attribute value
   */
  attributeChangedCallback(name, oldValue, newValue) {
    switch (name){
      case 'label':
        let label = this.shadowRoot.querySelector(this.formSelector+" #label");
        label.textContent = newValue;
        break;
      case 'header':
        let header = this.shadowRoot.querySelector(this.formSelector+" #header");
        header.textContent = newValue;
        break;
      case 'options':
        let option = this.shadowRoot.querySelector(this.formSelector+" #option-select");
        this.fillOptions(option, newValue);
        break;
    }
  }

  fillOptions(elem, options){
    const optionList = JSON.parse(options);

    $(elem).empty();
    [{city: 'Select City'}, ...optionList].forEach(item =>{
      $(elem).append(`<option value="${item.city}">${item.city}</option>`);
    })
  }

  addErrorMsg(msg) {
    let validationMasg = this.shadowRoot.querySelector(this.formSelector+" #errMsg");
    $(validationMasg).text(msg).show();
  }

  hideErrorMsg() {
    let validationMasg = this.shadowRoot.querySelector(this.formSelector+" #errMsg");
    $(validationMasg).text('').hide();
  }
}

if ('customElements' in window) {
  customElements.define('choose-city', ChooseCity);
}
