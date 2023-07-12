import com.amazonaws.auth.AWSSessionCredentials
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.credentials.AwsCredentials
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*
import java.net.URI

class PublishPlugin : Plugin<Project> {
    private lateinit var parameters: PublishPluginExtension

    override fun apply(project: Project) {
        parameters = project.extensions.create("publish")
        project.afterEvaluate {
            val publication = buildPublication()
            project.applyPublication(publication)
        }
    }

    private fun Project.buildPublication(): Publication = Publication(
        name = parameters.publicationName!!,
        groupId = parameters.groupId!!,
        artifactId = parameters.artifactId!!,
        version = getVersionName(parameters.version!!),
        repositoryUri = getPublishRepoUri(),
        requiresCredentials = isExternal,
        publishSources = !isExternal
    )
}

private fun Project.getVersionName(version: String) =
    if (isSnapshot) "${version}-SNAPSHOT" else version

private fun Project.getPublishRepoUri(): URI = URI(
    when {
        isExternal -> "s3://x3m-artifact-registry/maven"
        isSnapshot -> "https://mavenrepo.etermax.com/artifactory/libs-snapshot-local"
        else -> "https://mavenrepo.etermax.com/artifactory/libs-release-local"
    }
)

private val Project.isExternal: Boolean
    get() = (project.rootProject.properties["isExternal"] as String).toBoolean()

private val Project.isSnapshot: Boolean
    get() = (project.rootProject.properties["isSnapshot"] as String).toBoolean()

open class PublishPluginExtension {
    var publicationName: String? = null
    var groupId: String? = null
    var artifactId: String? = null
    var version: String? = null
}

private data class Publication(
    val name: String,
    val groupId: String,
    val artifactId: String,
    val version: String,
    val repositoryUri: URI,
    val requiresCredentials: Boolean,
    val publishSources: Boolean
)

private data class Credentials(
    val accessKey: String,
    val secretKey: String,
    val sessionToken: String?
)

private fun Project.applyPublication(publication: Publication) {
    plugins.apply(MavenPublishPlugin::class.java)

    var sourcesJar: Jar? = null

    if (publication.publishSources) {
        val sourcesJar: Jar = task<Jar>("androidSourcesJar") {
            archiveClassifier.set("sources")
            from(android.sourceSets["main"].java.srcDirs)
        }

        artifacts {
            add("archives", sourcesJar)
        }
    }

    pluginManager.withPlugin("com.android.library") {
        publishing {
            publications {
                create<MavenPublication>(publication.name) {
                    from(components.getByName("release"))
                    sourcesJar?.let { artifact(it) }
                    groupId = publication.groupId
                    artifactId = publication.artifactId
                    version = publication.version
                }
            }
            repositories {
                maven {
                    url = publication.repositoryUri
                    if (publication.requiresCredentials) {
                        val awsCredentials = getAwsCredentials()
                        credentials(AwsCredentials::class) {
                            accessKey = awsCredentials.accessKey
                            secretKey = awsCredentials.secretKey
                            sessionToken = awsCredentials.sessionToken
                        }
                    }
                }
            }
        }
    }
}

private fun getAwsCredentials(): Credentials {
    val awsCredentials = DefaultAWSCredentialsProviderChain.getInstance().credentials
    return Credentials(
        accessKey = awsCredentials.awsAccessKeyId,
        secretKey = awsCredentials.awsSecretKey,
        sessionToken = (awsCredentials as? AWSSessionCredentials)?.run { sessionToken }
    )
}

private fun Project.publishing(configure: org.gradle.api.publish.PublishingExtension.() -> Unit): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("publishing", configure)

fun Project.publicationConfig(configure: PublishPluginExtension.() -> Unit): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure(configure)

val Project.android: com.android.build.gradle.LibraryExtension
    get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.gradle.LibraryExtension