import groovy.json.JsonSlurper

class Obj {
    def properties = Collections.synchronizedMap([:])
}

// Must add all properties before using the class for first time
addProperty("Author", Obj.class)
addProperty("Title", Obj.class)

def book2 = new Obj()
def propName = "Author"


book2.title = "The Hobbit"
println(book2.title)
book2.author = "Bill"
println(book2.author)
println (book2.properties)

println("===================")
def jsonSlurper = new JsonSlurper()
def jsonStr = '{"Title":"The Hobbit", "Author":"Bill"}'
def propBook = jsonSlurper.parseText(jsonStr)
//println(propBook.Title)
//println(propBook.Author)
//propBook.Title = "Not the hobbit"
//println(propBook.Title)

// Build a "dynamic" object on the fly, based on a map, with a custom setter
def book = new Expando()
book.properties = Collections.synchronizedMap([:])
for ( e in propBook ) {
    addProperty(e.key.toLowerCase(), book.class)
    book."${e.key.toLowerCase()}" = e.value
    }
// Note - this isn't quite working, because the setter isn't being called correctly
println("Book is:" + book.title)
println("DONE!")

private void addProperty(propName, Class clazz) {
    clazz.metaClass."set${propName}" = { String value ->
        delegate.properties[propName] = value
        println("Setter called for ${propName}=${value}")
    }
    clazz.metaClass."get$propName" = { ->
        delegate.properties[propName]
    }
}