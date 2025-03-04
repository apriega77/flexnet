apply(plugin = "maven-publish")

configure<PublishingExtension>{
    publications {
        create<MavenPublication>("release"){
            afterEvaluate {
                from(components.findByName("release"))
            }
            groupId = "com.flexnet"
            artifactId = project.name
            version = "1.0.2"
        }
    }
}