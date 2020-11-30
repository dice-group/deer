import React from "react";
import "litegraph.js/css/litegraph.css";

class MergeEnrichmentOperator extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showSelectors: true,
    };

    this.addInput("input", "text");
    this.properties = {
      name: "",
      node_1: "",
      node_2: "",
    };

    var that = this;

    this.addOutput("output", "text");
    this.size = [250, 90];
    this.title = "Merge Enrichment Operator";
    this.color = "#664d00";
    this.bgcolor = "#8c6a00";
    this.onDrawForeground = function(ctx, graphcanvas)
    {
      if(this.flags.collapsed)
        return;
      ctx.font = "14px Arial";
      ctx.fillText("Description of the node ...", 10, 40); 
    }
  }
}

export default MergeEnrichmentOperator;
