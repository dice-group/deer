import React, { Fragment } from "react";
import "litegraph.js/css/litegraph.css";

// reactstrap components
import {
  Row,
  Col,
  Button,
  Form,
  FormGroup,
  Input,
  Dropdown,
  DropdownItem,
  Label,
  CardBody,
  Card,
  CardTitle,
  CardFooter,
  DropdownMenu,
  DropdownToggle,
} from "reactstrap";

class DereferencingEnrichmentOperator extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showSelectors: true,
    };

    this.addInput("input", "text");
    this.properties = {
      name: "",
      lookUpPrefix: "",
      dereferencingProperty: "",
      importProperty: "",
    };

    var that = this;
    var show = true;

    this.addOutput("output", "text");
    this.size = [180, 90];
    this.title = "Dereferencing Enrichment Operator";
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
              <Label>Look Up Prefix</Label>
              <Input
                type="text"
                //placeholder="deer:lookUpPrefix"
              />
            </FormGroup>
            <FormGroup>
              <Label>Dereferencing Property</Label>
              <Input
                type="text"
                //placeholder="deer:dereferencingProperty"
              />
            </FormGroup>
            <FormGroup>
              <Label>Import Property</Label>
              <Input
                type="text"
                //placeholder="deer:importProperty"
              />
            </FormGroup>
          </Form>
        </CardBody>
        <CardFooter>
          <Button
            className="btn-round"
            color="primary"
            // onClick={this.addNewPrefixes}
          >
            Save
          </Button>
        </CardFooter>
      </Card>
    );
  }
}

export default DereferencingEnrichmentOperator;
