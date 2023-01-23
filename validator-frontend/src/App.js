import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";
import CustomerList from "./components/CustomerList";

function App() {
  return (
    <div>
      <nav className="navbar navbar-expand navbar-dark bg-dark">
        <h1 className="navbar-brand">Jumia phone Validator</h1>
      </nav>
      <div className="container mt-3">
        <CustomerList />
      </div>
    </div>
  );
}

export default App;
