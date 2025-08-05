
# ExplorerSegment

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **Long** | The unique identifier of this segment |  [optional]
**name** | **String** | The name of this segment |  [optional]
**climbCategory** | **Integer** | The category of the climb [0, 5]. Higher is harder ie. 5 is Hors catégorie, 0 is uncategorized in climb_category. If climb_category &#x3D; 5, climb_category_desc &#x3D; HC. If climb_category &#x3D; 2, climb_category_desc &#x3D; 3. |  [optional]
**climbCategoryDesc** | [**ClimbCategoryDescEnum**](#ClimbCategoryDescEnum) | The description for the category of the climb |  [optional]
**avgGrade** | **Float** | The segment&#39;s average grade, in percents |  [optional]
**startLatlng** | [**LatLng**](LatLng.md) |  |  [optional]
**endLatlng** | [**LatLng**](LatLng.md) |  |  [optional]
**elevDifference** | **Float** | The segments&#39;s evelation difference, in meters |  [optional]
**distance** | **Float** | The segment&#39;s distance, in meters |  [optional]
**points** | **String** | The polyline of the segment |  [optional]


<a name="ClimbCategoryDescEnum"></a>
## Enum: ClimbCategoryDescEnum
Name | Value
---- | -----
NC | &quot;NC&quot;
_4 | &quot;4&quot;
_3 | &quot;3&quot;
_2 | &quot;2&quot;
_1 | &quot;1&quot;
HC | &quot;HC&quot;



