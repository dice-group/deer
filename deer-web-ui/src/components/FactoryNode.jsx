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

    this.onDrawForeground = function(ctx, graphcanvas)
    {
      if(this.flags.collapsed)
        return;
      ctx.font = "14px Arial";
      let str0 = "Description of the node ...Continue description of the node ...";
      let str1 = this.splitStringForCanvas(str0);
      var lines = str1.split('\n');
      for (var j = 0; j<lines.length; j++)
        ctx.fillText(lines[j], 10, 40 + (j*14) );
    }
    
  }

  // create folder for utils and move this function there
  splitStringForCanvas(str){
    let newStr = "";
    let charsInStr = 27;
    let myReg = new RegExp(".{1,"+charsInStr+"}", "g");
    let strArr = str.match(myReg);
    for(let i=0; i<strArr.length; i++){
      newStr += strArr[i].trim() + "\n";
    }
    return newStr;
  }

  onExecute() {
    let a = this.getInputData(0);
    if (a === undefined) {
      a = 0;
    }
    //console.log(a);
    this.setOutputData(0, ++a);
  }

  handleChange = (event) => {
    let value = event.target.value;
    this.setState({
      [event.target.name]: value,
    });
  };

  submitForm = () => {
    var properties = {
      node: FactoryNode,
      name: this.state["name"],
      outputFile: this.state["outputFile"],
      outputFormat: this.state["outputFormat"],
    };

    this.props.parentCallback(properties);
  };

  render() {
    return (
      <Card className="card-stats">
        <div className="numbers">
          <CardTitle tag="p">Node details</CardTitle>
          <p />
        </div>
        <CardBody>
          <Form>
            <FormGroup>
              <Label>Name</Label>
              <Input
                type="text"
                //placeholder="Node name"
                onChange={this.handleChange}
                name="name"
                id="name"
              />
            </FormGroup>
            <FormGroup>
              <Label>Output File</Label>
              <Input
                type="text"
                //placeholder="deer:outputFile"
                onChange={this.handleChange}
                name="outputFile"
                id="outputFile"
              />
            </FormGroup>
            <FormGroup>
              <Label>Output Format</Label>
              <Input
                type="text"
                //placeholder="deer:outputFormat"
                onChange={this.handleChange}
                name="outputFormat"
                id="outputFormat"
              />
            </FormGroup>
          </Form>
        </CardBody>
        <CardFooter>
          <Button
            className="btn-round"
            color="primary"
            onClick={this.submitForm}
          >
            Save
          </Button>
        </CardFooter>
      </Card>
    );
  }
}

export default FactoryNode;
