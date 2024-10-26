package com.rosan.installer.ui.page.installer.dialog.inner

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowRight
import androidx.compose.material.icons.twotone.AutoFixHigh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.rosan.installer.R
import com.rosan.installer.data.app.model.entity.AppEntity
import com.rosan.installer.data.app.util.InstalledAppInfo
import com.rosan.installer.data.app.util.sortedBest
import com.rosan.installer.data.installer.repo.InstallerRepo
import com.rosan.installer.ui.page.installer.dialog.DialogInnerParams
import com.rosan.installer.ui.page.installer.dialog.DialogParams
import com.rosan.installer.ui.page.installer.dialog.DialogParamsType
import com.rosan.installer.ui.page.installer.dialog.DialogViewAction
import com.rosan.installer.ui.page.installer.dialog.DialogViewModel
import org.koin.compose.getKoin

var lastInstalledAppInfo: InstalledAppInfo? = null

@Composable
fun InstallInfoDialog(
    installer: InstallerRepo,
    viewModel: DialogViewModel,
    onTitleExtraClick: ()-> Unit = {}
): DialogParams {
    val context: Context =
        getKoin().get()
    val entities =
        installer.entities.filter {it.selected}
            .map {it.app}
            .sortedBest()
    val entity =
        entities.first()
    val installed =
        InstalledAppInfo.buildByPackageName(
            entity.packageName
        )
//    if (installed != null && installedAppInfo == null || installedAppInfo?.packageName != entity.packageName) {
//        installedAppInfo = installed
//    }
    val currentTimestamp =
        System.currentTimeMillis()
    var installedJustNow =
        installed?.lastUpdateTime?.let {
            currentTimestamp - it < 2000
        }
            ?: false
    if (!installedJustNow) {
        lastInstalledAppInfo = installed
    }
    return DialogParams(
        icon = DialogInnerParams(
            DialogParamsType.InstallerInfo.id
        ) {
            Image(
                modifier = Modifier
                    .size(
                        64.dp
                    )
                    .clip(
                        RoundedCornerShape(
                            12.dp
                        )
                    ),
                painter = rememberDrawablePainter(
                    (if (entity is AppEntity.BaseEntity) entity.icon
                    else installed?.icon)
                        ?: ContextCompat.getDrawable(
                            context,
                            android.R.drawable.sym_def_app_icon
                        )
                ),
                contentDescription = null
            )
        },
        title = DialogInnerParams(
            DialogParamsType.InstallerInfo.id
        ) {
            Box {
                Text(
                    (if (entity is AppEntity.BaseEntity) entity.label
                    else installed?.label)
                        ?: when (entity) {
                            is AppEntity.SplitEntity -> entity.splitName
                            is AppEntity.DexMetadataEntity -> entity.dmName
                            else -> entity.packageName
                        },
                    modifier = Modifier
                        .align(
                            Alignment.CenterEnd
                        )
                        .absolutePadding(
                            right = 32.dp
                        )
                        .basicMarquee()
                )
                IconButton(
                    modifier = Modifier
                        .align(
                            Alignment.CenterEnd
                        )
                        .clip(
                            CircleShape
                        )
                        .background(
                            MaterialTheme.colorScheme.primaryContainer
                        )
                        .size(
                            24.dp
                        ),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    onClick = onTitleExtraClick
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.AutoFixHigh,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(
                                4.dp
                            )
                    )
                }
            }
        },
        subtitle = DialogInnerParams(
            DialogParamsType.InstallerInfo.id
        ) {
            Column {
                if (entity is AppEntity.BaseEntity)
//                    if (installed == null || installedJustNow) Row(
                    if (installed == null || lastInstalledAppInfo == null) Row(
                        modifier = Modifier
                            .align(
                                Alignment.CenterHorizontally
                            )
                    ) {
                        Text(
                            stringResource(
                                R.string.installer_version,
                            ),
                            modifier = Modifier.align(
                                Alignment.CenterVertically
                            )
                        )
                        Text(
                            stringResource(
                                R.string.installer_version2,
                                entity.versionName,
                                entity.versionCode
                            ),
                            modifier = Modifier.align(
                                Alignment.CenterVertically
                            )
                        )
                    }
                    else Column(
                        modifier = Modifier
                            .align(
                                Alignment.CenterHorizontally
                            )
                            .padding(
                                0.dp,
                                4.dp
                            )
                            .basicMarquee()
                    ) {
                        Row(
                            modifier = Modifier
                                .align(
                                    Alignment.Start
                                )
                        ) {
                            Text(
                                stringResource(
                                    R.string.installer_version,
                                ),
                                modifier = Modifier.align(
                                    Alignment.CenterVertically
                                )
                            )
                            Text(
                                stringResource(
                                    R.string.installer_version2,
                                    if (installedJustNow) lastInstalledAppInfo?.versionName
                                        ?: entity.versionName
                                    else installed.versionName,
                                    if (installedJustNow) lastInstalledAppInfo?.versionCode
                                        ?: entity.versionCode
                                    else installed.versionCode
                                ),
                                modifier = Modifier.align(
                                    Alignment.CenterVertically
                                )
                            )
                            Icon(
                                modifier = Modifier
                                    .size(
                                        24.dp
                                    )
                                    .align(
                                        Alignment.CenterVertically
                                    ),
                                imageVector = Icons.TwoTone.ArrowRight,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                        }
                        Row(
                            modifier = Modifier.align(
                                Alignment.Start
                            )
                        ) {
                            Text(
                                stringResource(
                                    R.string.installer_version,
                                ),
                                modifier = Modifier
                                    .align(
                                        Alignment.CenterVertically
                                    )
                                    .alpha(
                                        0f
                                    )
                            )
                            Text(
                                stringResource(
                                    R.string.installer_version2,
                                    entity.versionName,
                                    entity.versionCode
                                ),
                                modifier = Modifier.align(
                                    Alignment.CenterVertically
                                )
                            )
                            Icon(
                                modifier = Modifier
                                    .size(
                                        24.dp
                                    )
                                    .align(
                                        Alignment.CenterVertically
                                    )
                                    .alpha(
                                        0f
                                    ),
                                imageVector = Icons.TwoTone.ArrowRight,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null

                            )
                        }
                    }

                Text(
                    stringResource(
                        R.string.installer_package_name,
                        entity.packageName
                    ),
                    modifier = Modifier
                        .align(
                            Alignment.CenterHorizontally
                        )
                        .basicMarquee()
                )
            }
        },
        buttons = DialogButtons(
            DialogParamsType.ButtonsCancel.id
        ) {
            listOf(
                DialogButton(
                    stringResource(
                        R.string.cancel
                    )
                ) {
                    viewModel.dispatch(
                        DialogViewAction.Close
                    )
                })
        })
}