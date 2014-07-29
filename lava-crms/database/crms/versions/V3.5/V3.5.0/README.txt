lava-crms-data.sql does not cut it because of issue with the
InstrumentVersions list which has listvalues in crms-nacc for a list in crms so when trying to delete all crms lists in list a FK constraint violation results

This would also happen if app level listvalues where added for a crms list

In the end, just go with the additional metadata inserts
