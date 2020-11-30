import React, { Fragment } from "react";
import "litegraph.js/css/litegraph.css";
import Select from "react-select";

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

const options = [
  { value: "all", label: "all" },
  { value: "best1toN", label: "best1toN" },
  { value: "best1to1", label: "best1to1" },
  { value: "best", label: "best" },
];

class LinkingEnrichmentOperator extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showSelectors: true,
      selectedOption: null,
    };
    this.addOutput("output1", "text");
    this.addInput("input1", "text");
    this.addInput("input2", "text");

    this.properties = {
      name: "",
      specFile: "",
      linksPart: "",
      linkSpecification: "",
      linkingPredicate: "",
      threshold: "",
    };

    var that = this;

    this.widgets_up = true;
    this.size = [240, 90];
    
    this.title = "Linking Enrichment Operator";
    this.color = "#664d00";
    this.bgcolor = "#8c6a00";
    this.onDrawForeground = function(ctx, graphcanvas)
    {
      if(this.flags.collapsed)
        return;
      ctx.font = "14px Arial";
      // ctx.fillStyle = "#000";
      ctx.fillText("Description of the node ...", 10, 60); 
    }
  }

  toggle = () => {
    this.setState((prevState) => ({
      dropdownOpen: !prevState.dropdownOpen,
    }));
  };

  handleSelectChange = (selectedOption) => {
    this.setState({ selectedOption, selectMode: selectedOption.value });
  };

  submitForm = () => {
    var properties = {
      node: LinkingEnrichmentOperator,
      name: this.state["name"],
      specFile: this.state["specFile"],
      linksPart: this.state["radioOption"],
      selectMode: this.state["selectMode"],
      linkSpecification: this.state["linkSpecification"],
      linkingPredicate: this.state["linkingPredicate"],
      threshold: this.state["threshold"],
    };

    this.props.parentCallback(properties);
  };

  handleChange = (event) => {
    let value = event.target.value;
    this.setState({
      [event.target.name]: value,
    });
  };

  handleRadioChange = (event) => {
    this.setState({
      radioOption: event.target.value,
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
                onChange={this.handleChange}
                //der="Node name"
                name="name"
                id="name"
              />
            </FormGroup>
            <FormGroup>
              <Label>Spec File:</Label>
              <Input
                type="text"
                //placeholder="deer:specFile"
                onChange={this.handleChange}
                name="specFile"
                id="specFile"
              />
            </FormGroup>
            <FormGroup tag="fieldset">
              <legend>LinksPart</legend>
              <FormGroup check>
                <Label check>
                  <Input
                    type="radio"
                    name="radio1"
                    value="source"
                    onChange={this.handleRadioChange}
                    checked={this.state.radioOption === "source"}
                  />{" "}
                  Source
                </Label>
              </FormGroup>
              <FormGroup check>
                <Label check>
                  <Input
                    type="radio"
                    name="radio1"
                    value="target"
                    onChange={this.handleRadioChange}
                    checked={this.state.radioOption === "target"}
                  />{" "}
                  Target
                </Label>
              </FormGroup>
            </FormGroup>
            <FormGroup>
              <Label>Select Mode</Label>
              <Select
                value={this.state.selectedOption}
                onChange={this.handleSelectChange}
                options={options}
                name="selectMode"
                id="selectMode"
              />
            </FormGroup>
            <FormGroup>
              <Label>Link Specification:</Label>
              <Input
                type="text"
                placeholder="deer:linkSpecification"
                onChange={this.handleChange}
                name="linkSpecification"
                id="linkSpecification"
              />
            </FormGroup>
            <FormGroup>
              <Label>Linking Predicate:</Label>
              <Input
                type="text"
                //placeholder="deer:linkingPredicate"
                onChange={this.handleChange}
                name="linkingPredicate"
                id="linkingPredicate"
              />
            </FormGroup>
            <FormGroup>
              <Label>Threshold:</Label>
              <Input
                type="text"
                //placeholder="deer:threshold"
                onChange={this.handleChange}
                name="threshold"
                id="threshold"
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

export default LinkingEnrichmentOperator;
