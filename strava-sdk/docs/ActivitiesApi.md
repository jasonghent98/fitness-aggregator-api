# ActivitiesApi

All URIs are relative to *https://www.strava.com/api/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createActivity**](ActivitiesApi.md#createActivity) | **POST** /activities | Create an Activity
[**getActivityById**](ActivitiesApi.md#getActivityById) | **GET** /activities/{id} | Get Activity
[**getCommentsByActivityId**](ActivitiesApi.md#getCommentsByActivityId) | **GET** /activities/{id}/comments | List Activity Comments
[**getKudoersByActivityId**](ActivitiesApi.md#getKudoersByActivityId) | **GET** /activities/{id}/kudos | List Activity Kudoers
[**getLapsByActivityId**](ActivitiesApi.md#getLapsByActivityId) | **GET** /activities/{id}/laps | List Activity Laps
[**getLoggedInAthleteActivities**](ActivitiesApi.md#getLoggedInAthleteActivities) | **GET** /athlete/activities | List Athlete Activities
[**getZonesByActivityId**](ActivitiesApi.md#getZonesByActivityId) | **GET** /activities/{id}/zones | Get Activity Zones
[**updateActivityById**](ActivitiesApi.md#updateActivityById) | **PUT** /activities/{id} | Update Activity


<a name="createActivity"></a>
# **createActivity**
> DetailedActivity createActivity(name, sportType, startDateLocal, elapsedTime, type, description, distance, trainer, commute)

Create an Activity

Creates a manual activity for an athlete, requires activity:write scope.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ActivitiesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: strava_oauth
OAuth strava_oauth = (OAuth) defaultClient.getAuthentication("strava_oauth");
strava_oauth.setAccessToken("YOUR ACCESS TOKEN");

ActivitiesApi apiInstance = new ActivitiesApi();
String name = "name_example"; // String | The name of the activity.
String sportType = "sportType_example"; // String | Sport type of activity. For example - Run, MountainBikeRide, Ride, etc.
OffsetDateTime startDateLocal = OffsetDateTime.now(); // OffsetDateTime | ISO 8601 formatted date time.
Integer elapsedTime = 56; // Integer | In seconds.
String type = "type_example"; // String | Type of activity. For example - Run, Ride etc.
String description = "description_example"; // String | Description of the activity.
Float distance = 3.4F; // Float | In meters.
Integer trainer = 56; // Integer | Set to 1 to mark as a trainer activity.
Integer commute = 56; // Integer | Set to 1 to mark as commute.
try {
    DetailedActivity result = apiInstance.createActivity(name, sportType, startDateLocal, elapsedTime, type, description, distance, trainer, commute);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ActivitiesApi#createActivity");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **name** | **String**| The name of the activity. |
 **sportType** | **String**| Sport type of activity. For example - Run, MountainBikeRide, Ride, etc. |
 **startDateLocal** | **OffsetDateTime**| ISO 8601 formatted date time. |
 **elapsedTime** | **Integer**| In seconds. |
 **type** | **String**| Type of activity. For example - Run, Ride etc. | [optional]
 **description** | **String**| Description of the activity. | [optional]
 **distance** | **Float**| In meters. | [optional]
 **trainer** | **Integer**| Set to 1 to mark as a trainer activity. | [optional]
 **commute** | **Integer**| Set to 1 to mark as commute. | [optional]

### Return type

[**DetailedActivity**](DetailedActivity.md)

### Authorization

[strava_oauth](../README.md#strava_oauth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getActivityById"></a>
# **getActivityById**
> DetailedActivity getActivityById(id, includeAllEfforts)

Get Activity

Returns the given activity that is owned by the authenticated athlete. Requires activity:read for Everyone and Followers activities. Requires activity:read_all for Only Me activities.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ActivitiesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: strava_oauth
OAuth strava_oauth = (OAuth) defaultClient.getAuthentication("strava_oauth");
strava_oauth.setAccessToken("YOUR ACCESS TOKEN");

ActivitiesApi apiInstance = new ActivitiesApi();
Long id = 789L; // Long | The identifier of the activity.
Boolean includeAllEfforts = true; // Boolean | To include all segments efforts.
try {
    DetailedActivity result = apiInstance.getActivityById(id, includeAllEfforts);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ActivitiesApi#getActivityById");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The identifier of the activity. |
 **includeAllEfforts** | **Boolean**| To include all segments efforts. | [optional]

### Return type

[**DetailedActivity**](DetailedActivity.md)

### Authorization

[strava_oauth](../README.md#strava_oauth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getCommentsByActivityId"></a>
# **getCommentsByActivityId**
> List&lt;Comment&gt; getCommentsByActivityId(id, page, perPage, pageSize, afterCursor)

List Activity Comments

Returns the comments on the given activity. Requires activity:read for Everyone and Followers activities. Requires activity:read_all for Only Me activities.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ActivitiesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: strava_oauth
OAuth strava_oauth = (OAuth) defaultClient.getAuthentication("strava_oauth");
strava_oauth.setAccessToken("YOUR ACCESS TOKEN");

ActivitiesApi apiInstance = new ActivitiesApi();
Long id = 789L; // Long | The identifier of the activity.
Integer page = 56; // Integer | Deprecated. Prefer to use after_cursor.
Integer perPage = 30; // Integer | Deprecated. Prefer to use page_size.
Integer pageSize = 30; // Integer | Number of items per page. Defaults to 30.
String afterCursor = "afterCursor_example"; // String | Cursor of the last item in the previous page of results, used to request the subsequent page of results.  When omitted, the first page of results is fetched.
try {
    List<Comment> result = apiInstance.getCommentsByActivityId(id, page, perPage, pageSize, afterCursor);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ActivitiesApi#getCommentsByActivityId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The identifier of the activity. |
 **page** | **Integer**| Deprecated. Prefer to use after_cursor. | [optional]
 **perPage** | **Integer**| Deprecated. Prefer to use page_size. | [optional] [default to 30]
 **pageSize** | **Integer**| Number of items per page. Defaults to 30. | [optional] [default to 30]
 **afterCursor** | **String**| Cursor of the last item in the previous page of results, used to request the subsequent page of results.  When omitted, the first page of results is fetched. | [optional]

### Return type

[**List&lt;Comment&gt;**](Comment.md)

### Authorization

[strava_oauth](../README.md#strava_oauth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getKudoersByActivityId"></a>
# **getKudoersByActivityId**
> List&lt;SummaryAthlete&gt; getKudoersByActivityId(id, page, perPage)

List Activity Kudoers

Returns the athletes who kudoed an activity identified by an identifier. Requires activity:read for Everyone and Followers activities. Requires activity:read_all for Only Me activities.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ActivitiesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: strava_oauth
OAuth strava_oauth = (OAuth) defaultClient.getAuthentication("strava_oauth");
strava_oauth.setAccessToken("YOUR ACCESS TOKEN");

ActivitiesApi apiInstance = new ActivitiesApi();
Long id = 789L; // Long | The identifier of the activity.
Integer page = 56; // Integer | Page number. Defaults to 1.
Integer perPage = 30; // Integer | Number of items per page. Defaults to 30.
try {
    List<SummaryAthlete> result = apiInstance.getKudoersByActivityId(id, page, perPage);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ActivitiesApi#getKudoersByActivityId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The identifier of the activity. |
 **page** | **Integer**| Page number. Defaults to 1. | [optional]
 **perPage** | **Integer**| Number of items per page. Defaults to 30. | [optional] [default to 30]

### Return type

[**List&lt;SummaryAthlete&gt;**](SummaryAthlete.md)

### Authorization

[strava_oauth](../README.md#strava_oauth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getLapsByActivityId"></a>
# **getLapsByActivityId**
> List&lt;Lap&gt; getLapsByActivityId(id)

List Activity Laps

Returns the laps of an activity identified by an identifier. Requires activity:read for Everyone and Followers activities. Requires activity:read_all for Only Me activities.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ActivitiesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: strava_oauth
OAuth strava_oauth = (OAuth) defaultClient.getAuthentication("strava_oauth");
strava_oauth.setAccessToken("YOUR ACCESS TOKEN");

ActivitiesApi apiInstance = new ActivitiesApi();
Long id = 789L; // Long | The identifier of the activity.
try {
    List<Lap> result = apiInstance.getLapsByActivityId(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ActivitiesApi#getLapsByActivityId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The identifier of the activity. |

### Return type

[**List&lt;Lap&gt;**](Lap.md)

### Authorization

[strava_oauth](../README.md#strava_oauth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getLoggedInAthleteActivities"></a>
# **getLoggedInAthleteActivities**
> List&lt;SummaryActivity&gt; getLoggedInAthleteActivities(before, after, page, perPage)

List Athlete Activities

Returns the activities of an athlete for a specific identifier. Requires activity:read. Only Me activities will be filtered out unless requested by a token with activity:read_all.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ActivitiesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: strava_oauth
OAuth strava_oauth = (OAuth) defaultClient.getAuthentication("strava_oauth");
strava_oauth.setAccessToken("YOUR ACCESS TOKEN");

ActivitiesApi apiInstance = new ActivitiesApi();
Integer before = 56; // Integer | An epoch timestamp to use for filtering activities that have taken place before a certain time.
Integer after = 56; // Integer | An epoch timestamp to use for filtering activities that have taken place after a certain time.
Integer page = 56; // Integer | Page number. Defaults to 1.
Integer perPage = 30; // Integer | Number of items per page. Defaults to 30.
try {
    List<SummaryActivity> result = apiInstance.getLoggedInAthleteActivities(before, after, page, perPage);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ActivitiesApi#getLoggedInAthleteActivities");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **before** | **Integer**| An epoch timestamp to use for filtering activities that have taken place before a certain time. | [optional]
 **after** | **Integer**| An epoch timestamp to use for filtering activities that have taken place after a certain time. | [optional]
 **page** | **Integer**| Page number. Defaults to 1. | [optional]
 **perPage** | **Integer**| Number of items per page. Defaults to 30. | [optional] [default to 30]

### Return type

[**List&lt;SummaryActivity&gt;**](SummaryActivity.md)

### Authorization

[strava_oauth](../README.md#strava_oauth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getZonesByActivityId"></a>
# **getZonesByActivityId**
> List&lt;ActivityZone&gt; getZonesByActivityId(id)

Get Activity Zones

Summit Feature. Returns the zones of a given activity. Requires activity:read for Everyone and Followers activities. Requires activity:read_all for Only Me activities.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ActivitiesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: strava_oauth
OAuth strava_oauth = (OAuth) defaultClient.getAuthentication("strava_oauth");
strava_oauth.setAccessToken("YOUR ACCESS TOKEN");

ActivitiesApi apiInstance = new ActivitiesApi();
Long id = 789L; // Long | The identifier of the activity.
try {
    List<ActivityZone> result = apiInstance.getZonesByActivityId(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ActivitiesApi#getZonesByActivityId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The identifier of the activity. |

### Return type

[**List&lt;ActivityZone&gt;**](ActivityZone.md)

### Authorization

[strava_oauth](../README.md#strava_oauth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="updateActivityById"></a>
# **updateActivityById**
> DetailedActivity updateActivityById(id, body)

Update Activity

Updates the given activity that is owned by the authenticated athlete. Requires activity:write. Also requires activity:read_all in order to update Only Me activities

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ActivitiesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: strava_oauth
OAuth strava_oauth = (OAuth) defaultClient.getAuthentication("strava_oauth");
strava_oauth.setAccessToken("YOUR ACCESS TOKEN");

ActivitiesApi apiInstance = new ActivitiesApi();
Long id = 789L; // Long | The identifier of the activity.
UpdatableActivity body = new UpdatableActivity(); // UpdatableActivity | 
try {
    DetailedActivity result = apiInstance.updateActivityById(id, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ActivitiesApi#updateActivityById");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The identifier of the activity. |
 **body** | [**UpdatableActivity**](UpdatableActivity.md)|  | [optional]

### Return type

[**DetailedActivity**](DetailedActivity.md)

### Authorization

[strava_oauth](../README.md#strava_oauth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

