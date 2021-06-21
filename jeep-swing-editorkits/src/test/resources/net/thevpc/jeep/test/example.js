let student1 = {
    age: 24,
    name: "Felix"
}

const handler = {
    get: function(obj, prop) {
        return obj[prop] ? obj[prop] : 'property does not exist';
    }
}

const proxy = new Proxy(student1, handler);
console.log(proxy.name); // Felix
console.log(proxy.age); // 24
console.log(proxy.class); // property does not exist
let student = {
    name: 'Jack',
    age: 24
}

const handler = { };

// passing empty handler
const proxy1 = new Proxy(student, {});

console.log(proxy1); // Proxy {name: "Jack", age: 24}
console.log(proxy1.name); // Jack