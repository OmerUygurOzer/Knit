[![](https://jitpack.io/v/OmerUygurOzer/knit.svg)](https://jitpack.io/#OmerUygurOzer/knit)

# Knit
MVP Framework for Android apps. 

Features:

1. Event based system pattern as shown here : [Android MVP — Doing it “right”](https://android.jlelse.eu/android-mvp-doing-it-right-dac9d5d72079)  
2. Smart Presenters: they outlive the activities they are attached to and persist their states unless memory is low.
3. Contracts are auto-generated.
4. No need to tie components(Model,View,Presenter) together, all communication is handled under the hood.
5. Umbrella Models: Models can fetch data from other models and expose a single data type. 
6. View-States supported: User can handle configuration changes such as screen rotation.
7. Navigation is supported as outlined here : [Navigation in the context of MVP](https://medium.com/@nikita.kozlov/navigation-in-the-context-of-mvp-f474ed313901)  
8. Easy integration with other libraries such as Dagger.

Version 1.1.0  

- Supports multiple threads for generators to be executed on
- Usage tree for components
- Support for reporting errors from Generators added
- Test Kit added to make testing easy
- Smarter life-cycle callbacks(Removed Knit.show(view), Knit.dismiss(view))
- Bug fixes

### Adding Knit to the project:
```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
  implementation 'com.github.OmerUygurOzer.knit:knitlib:v1.1.0'
  annotationProcessor 'com.github.OmerUygurOzer.knit:knitprocessor:v1.1.0'
}
```


Models:
```java
@Model
public class RestLayer extends KnitModel {

    public static final String GET_REPOS = "i";

    @Inject
    ApiManager apiManager;

    @Override
    public void onCreate() {
        DaggerModelsComponent.create().inject(this);
    }

    @Generates(GET_REPOS)
    Generator1<List<Repo>,String> getRepos = new Generator1<List<Repo>,String>() {
        @Override
        public List<Repo> generate(String s) {
            try {
                return apiManager.accessCalls().listRepos(s).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
```

Views:
```java
@KnitView
public class InputActivity extends Activity{

    public static final String SEARCH_CLICK = "sc";

    EditText editText;

    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        editText = (EditText)findViewById(R.id.edit_text);
        button = (Button)findViewById(R.id.button);
        KnitEvents.onClick(SEARCH_CLICK,this, button);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public String getUserName(){
        return editText.getText().toString().trim();
    }


}
```

Presenters:
```java
@Presenter(RepoActivity.class)
public class RepoActivityPresenter extends KnitPresenter<RepoActivityContract> {


    @Override
    public void onCreate() {

    }

    @Override
    public void onViewApplied(Object o, Bundle bundle) {
        request(RestLayer.GET_REPOS, KnitSchedulers.IO,KnitSchedulers.MAIN,bundle.getString("userName"));
    }

    @Override
    public void onCurrentViewReleased() {

    }

    @Override
    public void handle(ViewEventPool viewEventPool, ViewEventEnv viewEventEnv,
            InternalModel internalModel) {

    }

    @Updating(RestLayer.GET_REPOS)
    public void repoRec(List<Repo> repos){
        getContract().showRepos(repos);
    }
}
```



