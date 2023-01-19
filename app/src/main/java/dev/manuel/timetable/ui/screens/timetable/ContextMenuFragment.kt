package dev.manuel.timetable.ui.screens.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.manuel.timetable.R
import dev.manuel.timetable.room.entities.Timetable
import dev.manuel.timetable.ui.components.DeleteMessageDialog

class ContextMenuFragment(
    private val timetable: Timetable,
    private val onEdit: () -> Unit = {},
    private val onDelete: (Timetable) -> Unit = {},
    private val onSelect: (Timetable) -> Unit = {},
) : BottomSheetDialogFragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_context_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val composeView = view.findViewById<ComposeView>(R.id.fragmentBottomSheetContextMenu)
        composeView.setContent {
            Mdc3Theme {
                ContextMenu(
                    selection = timetable,
                    onEdit = { onEdit(); dismiss() },
                    onDelete = { onDelete(timetable); dismiss() },
                    onSelect = { onSelect(timetable); dismiss() },
                    onDismiss = {}
                )
            }
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ContextMenu(
    selection: Timetable,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSelect: () -> Unit,
    onDismiss: () -> Unit,
) {

    //State for the dialog asking the user to confirm the deletion of the current timetable.
    var confirmDeletion by remember { mutableStateOf(false) }

    if (confirmDeletion) {
        DeleteMessageDialog(
            title = stringResource(id = R.string.confirm_delete_timetable_title),
            message = stringResource(id = R.string.confirm_delete_timetable_message),
            onConfirm = onDelete,
            onDismiss = { confirmDeletion = false }
        )
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            Text(
                text = selection.name,
                modifier = Modifier
                    .padding(all = 16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            ListItem(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null
                    )
                },
                modifier = Modifier.clickable { onSelect() },
            ) {
                Text(text = "Seleccionar horario")
            }
            ListItem(
                icon = { Icon(imageVector = Icons.Outlined.Edit, contentDescription = null) },
                modifier = Modifier.clickable { onEdit() },
            ) {
                Text(text = "Editar horario")
            }
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.error) {
                ListItem(
                    icon = { Icon(imageVector = Icons.Outlined.Delete, contentDescription = null) },
                    modifier = Modifier.clickable { confirmDeletion = true },
                ) {
                    Text(text = "Eliminar horario")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.cancel).uppercase())
                }
            }
        }
    }
}
