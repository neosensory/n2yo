[![](https://jitpack.io/v/neosensory/n2yo.svg)](https://jitpack.io/#neosensory/n2yo)

# n2yo

This Android library helps facilitate working with the [N2YO.com API](https://www.n2yo.com/api/) for satellite tracking. To use the API, you'll need to sign up on their site to obtain a free API key. 

## Installation

You can add this library to your Android project via [Jitpack](https://jitpack.io/#neosensory/n2yo). This library requires the Internet permission be added to your AndroidManifest.xml file.

## Usage

See the [JavaDoc](https://neosensory.github.io/n2yo/) on GitHub pages for all of the available methods.

See the example application packaged in this repository. Generally, you just need to create an N2YO object and register a broadcastReceiver to handle responses, which come as a Bundle with a JSONObject (containing the response from the API) and a CallIDd enum that indicates the N2YO API call corresponding to the received response. 

There are method equivalents for every API call provided on the [N2YO.com API](https://www.n2yo.com/api/) site.

