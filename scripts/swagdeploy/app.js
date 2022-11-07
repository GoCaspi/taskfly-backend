const postmanToOpenApi = require('postman-to-openapi')
const fs = require("fs")

postmanPath = fs.readdirSync(".").filter(fn => fn.endsWith(".postman_collection.json"))
postmanPath = postmanPath[0]
var outputPath = "./collection.yml"

async function convertPost2OAPI(postmanpath, outputpath){
    return new Promise((resolve, reject) => {
        postmanToOpenApi(postmanpath, outputpath, { defaultTag: 'General' })
            .then(result => {
                resolve(result)
            })
            .catch(err => {
                reject(err)
            })
    })
}

convertPost2OAPI(postmanPath, outputPath)





