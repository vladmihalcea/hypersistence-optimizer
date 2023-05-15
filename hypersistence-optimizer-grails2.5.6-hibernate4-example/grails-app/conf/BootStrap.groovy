import org.example.pomodoro.Tag;
import org.example.pomodoro.Task;

class BootStrap {

    def init = { servletContext ->
        def workTag = new Tag(name: "Work").save(failOnError: true)
        def homeTag = new Tag(name: "Home").save(failOnError: true)
        
        def task = new Task(
                summary: "Do 2nd intro screencast",
                details: "Create the second intro screencast for Grails",
                status: "Open",
                deadline: new Date())
        task.addToTags(workTag)
        task.addToTags(homeTag)
        task.save(failOnError: true)
        
        task = new Task(
                summary: "Shopping",
                details: "Do the grocery shopping - and don't forget the tomatoes!",
                status: "Open",
                deadline: new Date() + 15)
        task.addToTags(homeTag)
        task.save(failOnError: true)
        
        task = new Task(
                summary: "Finish Grails presentation",
                details: "The last presentation on Grails & AMQP for SpringOne 2GX needs finishing.",
                status: "Open",
                deadline: new Date() - 3)
        task.addToTags(workTag)
        task.save(failOnError: true)
        
        task = new Task(
                summary: "Another task",
                details: """\
Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod \
tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, \
quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu \
fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in \
culpa qui officia deserunt mollit anim id est laborum.""",
                status: "Open",
                deadline: new Date() + 4)
        task.addToTags(workTag)
        task.save(failOnError: true)
    }
    def destroy = {
    }
}