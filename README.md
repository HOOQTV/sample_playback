# HOOQ Sample Playback App

**Migrated to https://github.coupang.net/coupang/couplay-sample_playback**

##  CONFIGURATION
Please set parameters needed before using this sample app.
1. API KEY -> tv.hooq.sampleapp.commons.Config.java:API_KEY
2. Shared Keys -> tv.hooq.sampleapp.commons.Config.java:getSharedKeysHmac()
3. Conviva API Key -> tv.hooq.sampleapp.commons.Config.java:CONVIVA_API_KEY
4. Conviva Gateway URL -> tv.hooq.sampleapp.commons.Config.java:CONVIVA_GATEWAY_URL
5. COUNTRY CODE -> tv.hooq.sampleapp.commons.Config.java:COUNTRY_CODE

    Available country code : ID, IN, PH, SG, TH
6. IP ADDRESS -> tv.hooq.sampleapp.commons.Config.java:IP_ADDRESS

    Send the client IP Address, do not hard code the IP Address on real implementation

If you want to activate the user directly after registering, please update the config :
1. WITH_ACTIVATE (tv.hooq.sampleapp.commons.Config.java:WITH_ACTIVATE) must be `true`
2. SKU (tv.hooq.sampleapp.commons.Config.java:SKU) : update with suitable SKU


## TODO :
1. Update TVOD data structure, currently not support Parcelable
2. Update TVSHOW flow, currently cannot play TVSHOW because there is no episode selector
