import com.google.cloud.tools.jib.plugins.common.PropertyNames

plugins {
    id("com.google.cloud.tools.jib")
}

jib {
    from {
        image = "ustc-edu-cn.mirror.aliyuncs.com/library/openjdk:latest"
    }
    to {
        image = "192.168.2.29:5100/leo/erp:latest"
        auth {
            username = "leo"
            password = "leo"
        }
    }
    container {
        ports = listOf("8080")
        volumes = listOf("/app/data")
        jvmFlags = listOf(
            "-Duser.timezone=Asia/Shanghai",
            "--add-exports=java.base/sun.security.x509=ALL-UNNAMED",
            "--add-exports=java.base/sun.security.tools.keytool=ALL-UNNAMED",
        )
    }
    setAllowInsecureRegistries(true)
    System.setProperty("sendCredentialsOverHttp", true.toString())

    /* For Gitlab CI Cache */
    val isCiServer = System.getenv().containsKey("CI")
    if (isCiServer) {
        val jibCacheDir = rootDir.resolve("jib-cache").path
        System.setProperty(PropertyNames.BASE_IMAGE_CACHE, jibCacheDir)
        System.setProperty(PropertyNames.APPLICATION_CACHE, jibCacheDir)
    }
}
