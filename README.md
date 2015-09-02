# AlertManager
Convenient  manager pop-up alerts

## Download (Gradle)

```groovy
repositories {
    maven { url "https://jitpack.io" }
}

buildscript {
    repositories {
        maven { url "https://jitpack.io" }
    }
}

dependencies {
    compile 'com.github.e16din:AlertManager:1.0.0'
}
```

## Usage examples

```java
AlertManager.manager(context).showAlert("Show must go on!");

AlertManager.manager(context).showAlert(
                                        R.string.any_message,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //do something on "Ok" button click
                                            }
                                        });
```