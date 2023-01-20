import React, { useState, useEffect } from "react";
import CustomerService from "../services/CustomerService";

const CustomerList = () => {
  const [customers, setCustomers] = useState([]);
  const [country, setCountry] = useState("");
  const [state, setState] = useState("");

  const onChangeCountry = (e) => {
    const country = e.target.value;
    setCountry(country);
  };

  const onChangeState = (e) => {
    const state = e.target.value;
    setState(state);
  };

  const getRequestParams = (country, state) => {
    let params = {};
    if (country) {
      params["country"] = country;
    }
    if (state) {
      params["state"] = state;
    }
    return params;
  };

  const retrieveCustomers = () => {
    const params = getRequestParams(country, state);
    CustomerService.getAll(params)
      .then((response) => {
        setCustomers(response.data);
        console.log(customers);
      })
      .catch((e) => {
        console.log(e);
      });
  };

  useEffect(retrieveCustomers, []);

  return (
    <div className="list row">
      <div className="col-md-8">
        <div className="input-group mb-3">
          <select value={state} onChange={onChangeState}>
            <option value="">ALL</option>
            <option value="VALID">Valid</option>
            <option value="INVALID">Invalid</option>
          </select>
          <select value={country} onChange={onChangeCountry}>
            <option value="">ALL</option>
            <option value="CAMEROON">Cameroon</option>
            <option value="ETHIOPIA">Ethiopia</option>
            <option value="MOROCCO">Morocco</option>
            <option value="MOZAMBIQUE">Mozambique</option>
            <option value="UGANDA">Uganda</option>
          </select>
          <div className="input-group-append">
            <button
              className="btn btn-outline-secondary"
              type="button"
              onClick={retrieveCustomers}
            >
              Add filter
            </button>
          </div>
        </div>
      </div>
      <div>
        <table className="table">
          <thead>
            <tr>
              <th>Id</th>
              <th>Name</th>
              <th>Phone</th>
              <th>Country</th>
              <th>Phone validation</th>
            </tr>
          </thead>
          <tbody>
            {customers.map((customer) => (
              <tr key={customer.id}>
                <td>{customer.id}</td>
                <td>{customer.name}</td>
                <td>{customer.phone}</td>
                <td>{customer.country}</td>
                <td>{customer.phoneNumberState}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default CustomerList;
