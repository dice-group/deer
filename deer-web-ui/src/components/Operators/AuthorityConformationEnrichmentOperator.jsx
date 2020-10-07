import React, { Fragment } from "react";
import "litegraph.js/css/litegraph.css";

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

class AuthorityConformationEnrichmentOperator extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showSelectors: true,
    };

    this.addInput("input", "text");
    this.properties = {
      name: "",
      sourceSubjectAuthority: "",
      targetSubjectAuthority: "",
    };

    var that = this;
    var show = true;


    this.addOutput("output", "text");
    this.widgets_up = true;
    this.size = [320, 90];
    this.title = "Authority Conformation Enrichment Operator";
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
      node: AuthorityConformationEnrichmentOperator,
      name: this.state["name"],
      sourceSubjectAuthority: this.state["sourceSubjectAuthority"],
      targetSubjectAuthority: this.state["targetSubjectAuthority"],
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
              <Label>Source Subject Authority</Label>
              <Input
                type="text"
                //placeholder="deer:sourceSubjectAuthority"
                onChange={this.handleChange}
                name="sourceSubjectAuthority"
                id="sourceSubjectAuthority"
              />
            </FormGroup>
            <FormGroup>
              <Label>Target Subject Authority</Label>
              <Input
                type="text"
                //placeholder="deer:targetSubjectAuthority"
                onChange={this.handleChange}
                name="targetSubjectAuthority"
                id="targetSubjectAuthority"
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

export default AuthorityConformationEnrichmentOperator;
