OFACNameSearch
==============

Automate submission of OFAC list search request using Selenium Java driver.

This was originally developed to compare the result of OFAC name searchs to a commercial product so I can determine if there's any statistical signficance betweeen the two, and more importantly to find if a linear relationship could be discovered.

OFAC search site throttles the number of concurrent requests so this is not to be utilized as a replacement or full blown name searching solution.

Information on List Search Application (From OFAC web site) This Sanctions List Search application (“Sanctions List Search”) is designed to facilitate the use of the Specially Designated Nationals and Blocked Persons list (“SDN List”) and the Foreign Sanctions Evaders List (“FSE List”). The Sanctions List Search tool uses approximate string matching to identify possible matches between word or character strings as entered into Sanctions List Search, and any name or name component as it appears on the SDN List and/or the FSE List. Sanctions List Search has a slider-bar that may be used to set a threshold (i.e., a confidence rating) for the closeness of any potential match returned as a result of a user’s search. Sanctions List Search will detect certain misspellings or other incorrectly entered text, and will return near, or proximate, matches, based on the confidence rating set by the user via the slider-bar. OFAC does not provide recommendations with regard to the appropriateness of any specific confidence rating.

Sanctions List Search is one tool offered to assist users in utilizing the SDN list and/or the FSE List; use of Sanctions List Search is not a substitute for undertaking appropriate due diligence.

The use of Sanctions List Search does not limit any criminal or civil liability for any act undertaken as a result of, or in reliance on, such use.
