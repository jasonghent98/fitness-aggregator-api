# SegmentEffortsApi

All URIs are relative to *https://www.strava.com/api/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getEffortsBySegmentId**](SegmentEffortsApi.md#getEffortsBySegmentId) | **GET** /segment_efforts | List Segment Efforts
[**getSegmentEffortById**](SegmentEffortsApi.md#getSegmentEffortById) | **GET** /segment_efforts/{id} | Get Segment Effort


<a name="getEffortsBySegmentId"></a>
# **getEffortsBySegmentId**
> List&lt;DetailedSegmentEffort&gt; getEffortsBySegmentId(segmentId, startDateLocal, endDateLocal, perPage)

List Segment Efforts

Returns a set of the authenticated athlete&#39;s segment efforts for a given segment.  Requires subscription.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.SegmentEffortsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: strava_oauth
OAuth strava_oauth = (OAuth) defaultClient.getAuthentication("strava_oauth");
strava_oauth.setAccessToken("YOUR ACCESS TOKEN");

SegmentEffortsApi apiInstance = new SegmentEffortsApi();
Integer segmentId = 56; // Integer | The identifier of the segment.
OffsetDateTime startDateLocal = OffsetDateTime.now(); // OffsetDateTime | ISO 8601 formatted date time.
OffsetDateTime endDateLocal = OffsetDateTime.now(); // OffsetDateTime | ISO 8601 formatted date time.
Integer perPage = 30; // Integer | Number of items per page. Defaults to 30.
try {
    List<DetailedSegmentEffort> result = apiInstance.getEffortsBySegmentId(segmentId, startDateLocal, endDateLocal, perPage);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SegmentEffortsApi#getEffortsBySegmentId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **segmentId** | **Integer**| The identifier of the segment. |
 **startDateLocal** | **OffsetDateTime**| ISO 8601 formatted date time. | [optional]
 **endDateLocal** | **OffsetDateTime**| ISO 8601 formatted date time. | [optional]
 **perPage** | **Integer**| Number of items per page. Defaults to 30. | [optional] [default to 30]

### Return type

[**List&lt;DetailedSegmentEffort&gt;**](DetailedSegmentEffort.md)

### Authorization

[strava_oauth](../README.md#strava_oauth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getSegmentEffortById"></a>
# **getSegmentEffortById**
> DetailedSegmentEffort getSegmentEffortById(id)

Get Segment Effort

Returns a segment effort from an activity that is owned by the authenticated athlete. Requires subscription.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.SegmentEffortsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: strava_oauth
OAuth strava_oauth = (OAuth) defaultClient.getAuthentication("strava_oauth");
strava_oauth.setAccessToken("YOUR ACCESS TOKEN");

SegmentEffortsApi apiInstance = new SegmentEffortsApi();
Long id = 789L; // Long | The identifier of the segment effort.
try {
    DetailedSegmentEffort result = apiInstance.getSegmentEffortById(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SegmentEffortsApi#getSegmentEffortById");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| The identifier of the segment effort. |

### Return type

[**DetailedSegmentEffort**](DetailedSegmentEffort.md)

### Authorization

[strava_oauth](../README.md#strava_oauth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

