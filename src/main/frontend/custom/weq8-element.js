import { html, LitElement, css } from 'lit';

//import { WEQ8Runtime } from "https://cdn.skypack.dev/weq8@0.2.1";
import { WEQ8Runtime } from "weq8";
//import "https://cdn.skypack.dev/weq8@0.2.1/ui";
import "weq8/ui";

// https://vaadin.com/blog/creating-a-custom-component-with-lit
// https://open-wc.org/guides/knowledge/lit-element/lifecycle/
// https://kevinsimper.medium.com/document-queryselector-with-web-components-76a8be5bc59

class Weq8Element extends LitElement {
  static styles = css`
    body {
      background-color: #555;
      color: white;
    }
    weq8-ui {
      height: 400px;
      margin: 0 auto;
    }
  `;

  static get properties() {
    return {
      filterIndex: { type: Number },
      filterType: { type: String },
      filterFrequency: { type: String },
      filterQ: { type: String },
      filterGain: { type: String }
    }
  }

  constructor(audioctx, runtime) {
    super();
    this.audioctx = new AudioContext();
    this.runtime = new WEQ8Runtime(this.audioctx);
    this.runtime.connect(this.audioctx.destination);

    this.runtime.on("filtersChanged", (state) => {
      // state is a data structure you can store in a variable, or serialize to JSON.
      console.log(state);

      const event = new CustomEvent('filter-changed', {
        detail: state,
        composed: true,
        cancelable: true,
        bubbles: true
      });
      this.dispatchEvent(event);
    });
  }

  firstUpdated() {
    super.firstUpdated();

    let weq8Ui = document.querySelector('weq8-element').shadowRoot.querySelector('weq8-ui');
    weq8Ui.runtime = this.runtime;
  }

  update() {
    super.update();

    if (this.filterType != null) {
      this.runtime.setFilterType(this.filterIndex, this.filterType);
    }
  }

  setFilterType(filterNumber, type) {
    this.runtime.setFilterType(filterNumber, type);
  }

  toggleBypass(filterNumber, bypass) {
    this.runtime.toggleBypass(filterNumber, bypass);
  }

  setFilterFrequency(filterNumber, frequency) {
    this.runtime.setFilterFrequency(filterNumber, frequency);
  }

  setFilterQ(filterNumber, q) {
    this.runtime.setFilterQ(filterNumber, q);
  }

  setFilterGain(filterNumber, gain) {
    this.runtime.setFilterGain(filterNumber, gain);
  }

  render() {
    return html`
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>weq8</title>
  </head>
  <body>
    <weq8-ui view="allBands" />
  </body>
          `;
  }
}

window.customElements.define('weq8-element', Weq8Element);