import http from "../http-common";

const getAll = (params) => {
  return http.get("/customers", { params });
};

export default {
  getAll,
};
