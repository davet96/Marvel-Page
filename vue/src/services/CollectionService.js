import axios from 'axios';

const http = axios.create({
  baseURL: "http://localhost:8080"
});

export default {

  getAllCollections() {
    return http.get(`/collections`);
  },

  get(id) {
    return http.get(`/collections/${id}`);
  },

  addCollection(newCollection) {
    return http.post(`/collections/create`, newCollection);
  },

  getComics(id) {
    return http.get(`/collections/comics/${id}`);
  }



}