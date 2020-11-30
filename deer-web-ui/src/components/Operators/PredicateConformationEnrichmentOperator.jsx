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

class PredicateConformationEnrichmentOperator extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showSelectors: true,
    };

    this.addInput("input", "text");
    this.properties = {
      name: "",
      sourcePredicate: "",
      targetPredicate: "",
    };

    var that = this;
    var show = true;

    this.addOutput("output", "text");
    this.size = [320, 90];
    this.title = "Predicate Conformation Enrichment Operator";
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

  submitForm = () => {
    var properties = {
      node: PredicateConformationEnrichmentOperator,
      name: this.state["name"],
      sourcePredicate: this.state["sourcePredicate"],
      targetPredicate: this.state["targetPredicate"],
    };

    this.props.parentCallback(properties);
  };

  handleChange = (event) => {
    let value = event.target.value;
    this.setState({
      [event.target.name]: value,
    });
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
              <Label>Source Predicate</Label>
              <Input
                type="text"
                //placeholder="deer:sourcePredicate"
                onChange={this.handleChange}
                name="sourcePredicate"
                id="sourcePredicate"
              />
            </FormGroup>
            <FormGroup>
              <Label>Target Predicate</Label>
              <Input
                type="text"
                //placeholder="deer:targetPredicate"
                onChange={this.handleChange}
                name="targetPredicate"
                id="targetPredicate"
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

export default PredicateConformationEnrichmentOperator;
