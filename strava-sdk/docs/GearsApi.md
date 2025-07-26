# GearsApi

All URIs are relative to *https://www.strava.com/api/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getGearById**](GearsApi.md#getGearById) | **GET** /gear/{id} | Get Equipment


<a name="getGearById"></a>
# **getGearById**
> DetailedGear getGearById(id)

Get Equipment

Returns an equipment using its identifier.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.GearsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: strava_oauth
OAuth strava_oauth = (OAuth) defaultClient.getAuthentication("strava_oauth");
strava_oauth.setAccessToken("YOUR ACCESS TOKEN");

GearsApi apiInstance = new GearsApi();
String id = "id_example"; // String | The identifier of the gear.
try {
    DetailedGear result = apiInstance.getGearById(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling GearsApi#getGearById");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| The identifier of the gear. |

### Return type

[**DetailedGear**](DetailedGear.md)

### Authorization

[strava_oauth](../README.md#strava_oauth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

