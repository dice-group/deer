import React, { Fragment } from "react";
// reactstrap components
import {
  Row,
  Col,
  Button,
  Form,
  FormGroup,
  Input,
  Label,
  CardBody,
  Card,
  CardTitle,
  CardFooter,
} from "reactstrap";

class FactoryNode extends React.Component {
  constructor(props) {
    super(props);

    // this.addInput("input", "text");
    // this.properties = {
    //   linkName: "https://google.com",
    //   name: "",
    //   outputFile: "number",
    //   outputFormat: 0,
    // };
    
    this.linkName = "https://google.com";

    var that = this;

    this.linkWidget = this.addWidget(
      "button",
      "click the link",
      this.linkName,
      function (v) {
        if (!v) {
          return;
        }
        window.open( 
              that.linkName, "_blank"); 
      }
    );
    this.widgets_start_y = 70;
    this.widgets_up = false;

    this.onDrawForeground = (ctx, graphcanvas) =>
    {
      if(this.flags.collapsed)
        return;
      let fontSize = 10;
      ctx.font = fontSize+"px Arial";
      // console.log(this.message);
      let str0 = null;
      if(this.message[0]){
        str0 = this.message[0].replace(/"/g,"");
      }
      str0 = str0 || "Description of the node ...Continue description of the node ...";
      let str1 = this.splitStringForCanvas(str0);
      var lines = str1.split('\n');
      for (var j = 0; j<lines.length; j++)
        ctx.fillText(lines[j], 10, 40 + (j*fontSize) );
    }
    
  }

  // create folder for utils and move this function there
  splitStringForCanvas(str){
    let newStr = "";
    let charsInStr = 30;
    if(this.props.includes("Operator")){
      charsInStr = 60;
    }
    let myReg = new RegExp(".{1,"+charsInStr+"}", "g");
    let strArr = str.match(myReg);
    for(let i=0; i<strArr.length; i++){
      newStr += strArr[i].trim() + "\n";
    }
    return newStr;
  }

  render() {
    return (
      <div></div>
    );
  }
}

export default FactoryNode;
