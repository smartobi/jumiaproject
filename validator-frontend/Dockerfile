FROM node:alpine3.12
WORKDIR /validator-frontend
COPY ./build .
RUN npm i serve -g
CMD ["serve", "-s", "-l", "8081"]