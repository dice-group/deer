import React, { Fragment } from "react";
import "litegraph.js/css/litegraph.css";
import Select from "react-select";

// reactstrap components
import {
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

const options = [
  { value: "location", label: "Location" },
  { value: "person", label: "Person" },
  { value: "organization", label: "Organization" },
  { value: "all", label: "All" },
];

class NEREnrichmentOperator extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showSelectors: true,
      selectedOption: null,
    };

    this.addInput("input", "text");
    this.properties = {
      name: "",
      literalProperty: "",
      importProperty: "",
      neType: "location",
      foxUrl: false,
    };

    var that = this;

    this.addOutput("output", "text");
    this.size = [250, 90];
    this.title = "NER Enrichment Operator";
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

  handleSelectChange = (selectedOption) => {
    this.setState({ neType: selectedOption.value });
  };

  submitForm = () => {
    var properties = {
      node: NEREnrichmentOperator,
      name: this.state["name"],
      literalProperty: this.state["literalProperty"],
      importProperty: this.state["importProperty"],
      neType: this.state["neType"],
      foxUrl: this.state["foxUrl"],
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
          <CardTitle tag="h5">Node Details</CardTitle>
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
              <Label>Literal Property</Label>
              <Input
                type="text"
                //placeholder="deer:literalProperty"
                onChange={this.handleChange}
                name="literalProperty"
                id="literalProperty"
              />
            </FormGroup>
            <FormGroup>
              <Label>Import Property</Label>
              <Input
                type="text"
                //placeholder="deer:importProperty"
                onChange={this.handleChange}
                name="importProperty"
                id="importProperty"
              />
            </FormGroup>
            <FormGroup>
              <Label>neType</Label>
              <Select
                value={this.state.selectedOption}
                onChange={this.handleSelectChange}
                options={options}
                //placeholder="deer:neType"
                id="neType"
                name="neType"
              />
            </FormGroup>
            <FormGroup>
              <Label>Endpoint</Label>
              <Input
                type="text"
                //placeholder="deer:foxUrl"
                onChange={this.handleChange}
                name="foxUrl"
                id="foxUrl"
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

export default NEREnrichmentOperator;
